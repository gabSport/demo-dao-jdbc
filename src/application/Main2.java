package application;

import java.util.List;
import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class Main2 {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		
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
		
		/*
		System.out.println();
		System.out.println("==== TEST 3: department insert ====");
		Department newDep = new Department(null, "Music");
		depDao.insert(newDep);
		System.out.println("Inserted! New id = " + newDep.getId());
		*/
		
		System.out.println();
		System.out.println("==== TEST 4: department update ====");
		Department dep2 = depDao.findById(1); // carrego os dados do department cujo id = 1;
		dep2.setName("Food"); // setei para mudar o nome desse departamento 1 para "Food";
		depDao.update(dep2); // chamei o depDao.update para fazer a atualizacao do nome.
		System.out.println("Update completed!");
		
		System.out.println();
		System.out.println("==== TEST 5: department delete ====");
		System.out.print("Enter id for delete test: ");
		int id =  sc.nextInt();
		depDao.deleteById(id);
		System.out.println("Delete completed!");
	}
}
