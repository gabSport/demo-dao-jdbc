package application;

import java.util.List;
import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class Main2 {

	public static void main(String[] args) {
		
		
		DepartmentDao depDao = DaoFactory.createDepartmentDao(); // instancio o DepartmentDao chamando o DaoFactory, dessa forma, o programa conhece
		// a Interface, e nao a implementacao.
		
		System.out.println("==== TEST 1: department findById ====");
		Department dep = depDao.findById(1);
		System.out.println(dep);
		
		System.out.println();
		System.out.println("==== TEST 2: department findAll ====");
		List<Department> list = depDao.findAll();
		for (Department obj : list) {
			System.out.println(obj);
		}
		
		System.out.println();
		System.out.println("==== TEST 3: department insert ====");
		Department newDep = new Department(null, "Music");
		depDao.insert(newDep);
		System.out.println("Inserted! New id = " + newDep.getId());
	}
}
