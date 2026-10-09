-- Run this script in MySQL Workbench or the MySQL command line.
CREATE DATABASE IF NOT EXISTS online_job_portal;
USE online_job_portal;

CREATE TABLE IF NOT EXISTS jobs (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(120) NOT NULL,
    company VARCHAR(120) NOT NULL,
    location VARCHAR(120) NOT NULL,
    job_type VARCHAR(40) NOT NULL DEFAULT 'Full-time',
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS applications (
    id INT PRIMARY KEY AUTO_INCREMENT,
    job_id INT NOT NULL,
    applicant_name VARCHAR(120) NOT NULL,
    email VARCHAR(160) NOT NULL,
    phone VARCHAR(30),
    resume_link VARCHAR(500),
    applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_applications_jobs FOREIGN KEY (job_id)
        REFERENCES jobs(id) ON DELETE CASCADE
);

INSERT INTO jobs(title, company, location, job_type, description)
SELECT 'Java Developer Intern', 'TechNova Solutions', 'Noida', 'Internship',
       'Assist the team with Java application development.'
WHERE NOT EXISTS (SELECT 1 FROM jobs WHERE title='Java Developer Intern' AND company='TechNova Solutions');

INSERT INTO jobs(title, company, location, job_type, description)
SELECT 'Junior Software Developer', 'BrightByte Systems', 'Delhi', 'Full-time',
       'Work on backend features, testing and bug fixes.'
WHERE NOT EXISTS (SELECT 1 FROM jobs WHERE title='Junior Software Developer' AND company='BrightByte Systems');

INSERT INTO jobs(title, company, location, job_type, description)
SELECT 'QA Automation Trainee', 'QualityWorks', 'Remote', 'Remote',
       'Learn testing fundamentals and automation tools.'
WHERE NOT EXISTS (SELECT 1 FROM jobs WHERE title='QA Automation Trainee' AND company='QualityWorks');
