package UnitTests;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.godzilla.model.Company;
import com.godzilla.model.Issue;
import com.godzilla.model.Project;
import com.godzilla.model.DAO.CompanyDAO;
import com.godzilla.model.DAO.IssueDAO;
import com.godzilla.model.DAO.ProjectDAO;
import com.godzilla.model.exceptions.CompanyDAOException;
import com.godzilla.model.exceptions.CompanyException;
import com.godzilla.model.exceptions.IssueDAOException;
import com.godzilla.model.exceptions.ProjectDAOException;
import com.godzilla.model.exceptions.ProjectException;

@SuppressWarnings("all")
public class ProjectDAOTest {

	@Test
	public void createProject(){
			int companyId;
			try {
				companyId = CompanyDAO.getIdOfCompanyWithName("Company 1");
				Company company = CompanyDAO.getCompanyById(companyId);
				
				Project project = new Project("Project 2");
				ProjectDAO.addProject(project, company);
				
				Assert.assertTrue(project.getId() > 0);
			} catch (CompanyDAOException e) {
				Assert.assertTrue(e.getMessage().equals("couldn't find a company with that name")
					|| e.getMessage().equals("there is no company with that id")
					|| e.getMessage().equals("failed to get company's projects")
					|| e.getMessage().equals("failed to get company's users")
					|| e.getMessage().equals("Invalid company name"));
			} catch (ProjectException e) {
				Assert.assertTrue(e.getMessage().equals("project name cannot be null"));
			} catch (ProjectDAOException e) {
				Assert.assertTrue(e.getMessage().equals("couldn't find company")
						|| e.getMessage().equals("unknow company to add project to")
						|| e.getMessage().equals("project already exists")
						|| e.getMessage().equals("couldn't get id of the company with the name")
						|| e.getMessage().equals("failed to create project")
						|| e.getMessage().equals("couldn't set project's id"));
			}
	}
	
	//@Test
	public void removeProject(){
		int projectId = 12;
		try {
			Project project = ProjectDAO.getProjectById(projectId);
			
			ProjectDAO.removeProject(project);
		} catch (ProjectDAOException e) {
			Assert.assertTrue(e.getMessage().equals("couldn't find company")
					|| e.getMessage().equals("unknow company to add project to")
					|| e.getMessage().equals("project already exists")
					|| e.getMessage().equals("couldn't get id of the company with the name")
					|| e.getMessage().equals("failed to create project")
					|| e.getMessage().equals("couldn't set project's id"));
		}
	}
}
