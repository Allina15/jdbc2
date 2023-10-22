package Service.ServiceImpl;

import Dao.DaoImpl.JobDaoImpl;
import Dao.JobDao;
import Service.JobService;
import model.Job;

import java.util.List;

public class JobServImpl implements JobService {
    JobDao jobDao = new JobDaoImpl();
    @Override
    public void createJobTable() {
     jobDao.createJobTable();
    }

    @Override
    public void addJob(Job job) {
    jobDao.addJob(job);
    }

    @Override
    public Job getJobById(Long jobId)
    {
        return jobDao.getJobById(jobId);
    }
    @Override
    public List<Job> sortByExperience(String ascOrDesc) {
        return jobDao.sortByExperience(ascOrDesc);
    }

    @Override
    public Job getJobByEmployeeId(Long employeeId) {
        return jobDao.getJobByEmployeeId(employeeId);
    }

    @Override
    public void deleteDescriptionColumn() {
     jobDao.deleteDescriptionColumn();
    }
}
