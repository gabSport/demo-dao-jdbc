package model.dao;

import java.util.List;

import model.entities.Department;

public interface DepartmentDao {
	
	void insert(Department obj);
	void update(Department obj);
	void deleteById(Integer id);
	Department findById(Integer id); // essa operacao sera responsavel pegar esse id e consultar no banco de dados obj com esse id;
	//se existir, vai retornar, senao, retorna null;
	List<Department> findAll(); // para retornar todos os departamentos. tem que ser uma List
	

}
