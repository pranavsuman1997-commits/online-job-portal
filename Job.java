/**
 * Model class representing a job vacancy.
 */
public class Job {
    private final int id;
    private final String title;
    private final String company;
    private final String location;
    private final String jobType;
    private final String description;

    public Job(int id, String title, String company, String location,
               String jobType, String description) {
        this.id = id;
        this.title = title;
        this.company = company;
        this.location = location;
        this.jobType = jobType;
        this.description = description;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getCompany() { return company; }
    public String getLocation() { return location; }
    public String getJobType() { return jobType; }
    public String getDescription() { return description; }

    @Override
    public String toString() {
        return title + " — " + company + " (" + location + ")";
    }
}
