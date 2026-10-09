import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Beginner-friendly Java Swing desktop prototype for an Online Job Portal.
 * Features: list/search jobs, add jobs, apply to a selected job, view applications.
 */
public class JobPortalApp extends JFrame {
    private final DefaultTableModel jobModel = new DefaultTableModel(
            new String[]{"ID", "Job Title", "Company", "Location", "Type"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };
    private final JTable jobTable = new JTable(jobModel);
    private final JTextField searchField = new JTextField(20);
    private final JLabel statusLabel = new JLabel("Ready");

    public JobPortalApp() {
        super("Online Job Portal — Review 1 Prototype");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 580);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel heading = new JLabel("ONLINE JOB PORTAL", SwingConstants.CENTER);
        heading.setFont(new Font("SansSerif", Font.BOLD, 24));
        heading.setBorder(BorderFactory.createEmptyBorder(12, 8, 8, 8));
        add(heading, BorderLayout.NORTH);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Search title/company/location:"));
        topPanel.add(searchField);
        JButton searchButton = new JButton("Search");
        JButton allButton = new JButton("Show All");
        topPanel.add(searchButton);
        topPanel.add(allButton);

        jobTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jobTable.setRowHeight(24);
        JScrollPane tableScroll = new JScrollPane(jobTable);

        JPanel center = new JPanel(new BorderLayout(6, 6));
        center.add(topPanel, BorderLayout.NORTH);
        center.add(tableScroll, BorderLayout.CENTER);
        center.setBorder(BorderFactory.createEmptyBorder(0, 12, 8, 12));
        add(center, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        JButton addJobButton = new JButton("Add Job");
        JButton applyButton = new JButton("Apply to Selected Job");
        JButton applicationsButton = new JButton("View Applications");
        JButton refreshButton = new JButton("Refresh");
        buttons.add(addJobButton);
        buttons.add(applyButton);
        buttons.add(applicationsButton);
        buttons.add(refreshButton);

        JPanel south = new JPanel(new BorderLayout());
        south.add(buttons, BorderLayout.CENTER);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(4, 12, 8, 12));
        south.add(statusLabel, BorderLayout.SOUTH);
        add(south, BorderLayout.SOUTH);

        searchButton.addActionListener(e -> loadJobs(searchField.getText().trim()));
        searchField.addActionListener(e -> loadJobs(searchField.getText().trim()));
        allButton.addActionListener(e -> {
            searchField.setText("");
            loadJobs("");
        });
        refreshButton.addActionListener(e -> loadJobs(searchField.getText().trim()));
        addJobButton.addActionListener(e -> showAddJobDialog());
        applyButton.addActionListener(e -> showApplyDialog());
        applicationsButton.addActionListener(e -> showApplications());

        loadJobs("");
    }

    private void loadJobs(String query) {
        statusLabel.setText("Loading jobs...");
        new SwingWorker<List<Job>, Void>() {
            @Override protected List<Job> doInBackground() throws Exception {
                List<Job> jobs = new ArrayList<>();
                String sql = "SELECT id, title, company, location, job_type, description " +
                        "FROM jobs WHERE title LIKE ? OR company LIKE ? OR location LIKE ? ORDER BY id DESC";
                try (Connection con = DBConnection.getConnection();
                     PreparedStatement ps = con.prepareStatement(sql)) {
                    String pattern = "%" + query + "%";
                    ps.setString(1, pattern);
                    ps.setString(2, pattern);
                    ps.setString(3, pattern);
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            jobs.add(new Job(rs.getInt("id"), rs.getString("title"),
                                    rs.getString("company"), rs.getString("location"),
                                    rs.getString("job_type"), rs.getString("description")));
                        }
                    }
                }
                return jobs;
            }
            @Override protected void done() {
                try {
                    List<Job> jobs = get();
                    jobModel.setRowCount(0);
                    for (Job job : jobs) {
                        jobModel.addRow(new Object[]{job.getId(), job.getTitle(), job.getCompany(),
                                job.getLocation(), job.getJobType()});
                    }
                    statusLabel.setText(jobs.size() + " job(s) found.");
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    showError("Job loading was interrupted.");
                } catch (ExecutionException ex) {
                    showError("Could not load jobs. Check MySQL setup and DBConnection.java. " +
                            rootMessage(ex));
                }
            }
        }.execute();
    }

    private void showAddJobDialog() {
        JTextField title = new JTextField();
        JTextField company = new JTextField();
        JTextField location = new JTextField();
        JComboBox<String> type = new JComboBox<>(new String[]{"Full-time", "Part-time", "Internship", "Remote"});
        JTextArea description = new JTextArea(4, 24);
        JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));
        panel.add(new JLabel("Job title*")); panel.add(title);
        panel.add(new JLabel("Company*")); panel.add(company);
        panel.add(new JLabel("Location*")); panel.add(location);
        panel.add(new JLabel("Job type")); panel.add(type);
        panel.add(new JLabel("Description")); panel.add(new JScrollPane(description));

        int result = JOptionPane.showConfirmDialog(this, panel, "Add a Job",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) return;
        if (title.getText().trim().isEmpty() || company.getText().trim().isEmpty()
                || location.getText().trim().isEmpty()) {
            showError("Job title, company and location are required.");
            return;
        }

        String sql = "INSERT INTO jobs(title, company, location, job_type, description) VALUES(?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, title.getText().trim());
            ps.setString(2, company.getText().trim());
            ps.setString(3, location.getText().trim());
            ps.setString(4, (String) type.getSelectedItem());
            ps.setString(5, description.getText().trim());
            ps.executeUpdate();
            statusLabel.setText("Job added successfully.");
            loadJobs(searchField.getText().trim());
        } catch (SQLException ex) {
            showError("Could not add job: " + ex.getMessage());
        }
    }

    private void showApplyDialog() {
        int row = jobTable.getSelectedRow();
        if (row < 0) {
            showError("Please select a job from the table first.");
            return;
        }
        int modelRow = jobTable.convertRowIndexToModel(row);
        int jobId = (Integer) jobModel.getValueAt(modelRow, 0);
        String jobTitle = String.valueOf(jobModel.getValueAt(modelRow, 1));

        JTextField name = new JTextField();
        JTextField email = new JTextField();
        JTextField phone = new JTextField();
        JTextField resume = new JTextField();
        JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));
        panel.add(new JLabel("Selected job")); panel.add(new JLabel(jobTitle));
        panel.add(new JLabel("Full name*")); panel.add(name);
        panel.add(new JLabel("Email*")); panel.add(email);
        panel.add(new JLabel("Phone")); panel.add(phone);
        panel.add(new JLabel("Resume link (optional)")); panel.add(resume);

        int result = JOptionPane.showConfirmDialog(this, panel, "Apply for Job",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) return;
        if (name.getText().trim().isEmpty() || !email.getText().trim().contains("@")) {
            showError("Enter your name and a valid email address.");
            return;
        }

        String sql = "INSERT INTO applications(job_id, applicant_name, email, phone, resume_link) VALUES(?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, jobId);
            ps.setString(2, name.getText().trim());
            ps.setString(3, email.getText().trim());
            ps.setString(4, phone.getText().trim());
            ps.setString(5, resume.getText().trim());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Application submitted successfully!");
            statusLabel.setText("Application submitted for " + jobTitle);
        } catch (SQLException ex) {
            showError("Could not submit application: " + ex.getMessage());
        }
    }

    private void showApplications() {
        String sql = "SELECT a.id, a.applicant_name, a.email, j.title, a.applied_at " +
                "FROM applications a JOIN jobs j ON a.job_id = j.id ORDER BY a.id DESC";
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"ID", "Applicant", "Email", "Job", "Applied At"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                model.addRow(new Object[]{rs.getInt("id"), rs.getString("applicant_name"),
                        rs.getString("email"), rs.getString("title"), rs.getTimestamp("applied_at")});
            }
            JTable table = new JTable(model);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            JScrollPane scroll = new JScrollPane(table);
            scroll.setPreferredSize(new Dimension(720, 300));
            JOptionPane.showMessageDialog(this, scroll, "Job Applications", JOptionPane.PLAIN_MESSAGE);
        } catch (SQLException ex) {
            showError("Could not view applications: " + ex.getMessage());
        }
    }

    private String rootMessage(Exception ex) {
        Throwable cause = ex.getCause();
        return cause == null ? ex.getMessage() : cause.getMessage();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Online Job Portal",
                JOptionPane.ERROR_MESSAGE);
        statusLabel.setText("Action needs attention.");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
            catch (Exception ignored) { /* Use default look and feel. */ }
            new JobPortalApp().setVisible(true);
        });
    }
}
