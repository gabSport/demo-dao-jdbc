package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {
	
	private Connection conn; // vou ter esse obj conn a disposicao em qqr lugar da classe SellerDaoJdbc

	public SellerDaoJDBC(Connection conn) { // o nosso Dao vai ter uma dependencia com a conexao.
		this.conn = conn;
	}

	@Override
	public void insert(Seller obj) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void update(Seller obj) {
		// TODO Auto-generated method stub
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			
			st = conn.prepareStatement("SELECT seller.*, department.Name as DepName " // estou buscando todos os campos do seller + nome do departament
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE seller.id = ?");
			
			st.setInt(1, id);
			rs = st.executeQuery(); // o comando SQL acima vai ser executado e o resultado vai cair no rs
			if (rs.next()) {
				Department dep = instantiateDepartment(rs);
				Seller obj = instantiateSeller(rs, dep);
				return obj;
			}
			else {
				return null; // se nao existir nenhum vendedor com esse id, retorna nulo
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}	
	}
		
	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller obj = new Seller(); // instanciei um Seller, dpois setei seus valores abaixo:
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setBirthDate(rs.getDate("BirthDate"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setDepartment(dep);
		return obj;
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department(); // instanciei um Department, depois setei os valores dele abaixo:
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));
		return dep;
	}

	@Override
	public List<Seller> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			
			st = conn.prepareStatement("SELECT seller.* , department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE DepartmentId = ? "
					+ "ORDER BY Name");
			
			st.setInt(1, department.getId());
			
			rs = st.executeQuery();
			
			List<Seller> list = new ArrayList<Seller>();
			Map<Integer, Department> map = new HashMap<>();
			// a chave vai ser Integer - Id do departamento; e o valor vai ser Department
			
			while (rs.next()) { // pra cada valor do resultSet;
				
				Department dep = map.get(rs.getInt("DepartmentId")); // aqui estou testando se o Departamento ja existe
				// vou no map, tento buscar com o metodo get, algum departamento que tem esse id "DepartmentId"
				
				if (dep == null) { // se for null, siginifica que ainda nao existia;
					dep = instantiateDepartment(rs); // ai vou instanciar o departamento
					map.put(rs.getInt("DepartmentId"), dep); // agora vou salvar esse departamento dentro do map, para que na prox vez, 
					// eu possa verificar que ele ja existe
					
				}

				Seller obj = instantiateSeller(rs, dep); // o vendedor (Seller),
				list.add(obj); // adicionar esse vendedor a list.
			}
			return list; // no final, returna minha List.
			
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
}
