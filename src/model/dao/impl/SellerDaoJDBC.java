package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
		PreparedStatement st = null;
		try {
			
			st = conn.prepareStatement("INSERT INTO seller "
					+ "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS); // retornar o Id do novo Seller inserido
			
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId()); // Id do departamento "desse" vendedor (DepartmentId)
			
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) { // if pq estou inserido apenas um dado
					int id = rs.getInt(1); // posicao 1, pois vai ser a primeira coluna das chaves(Keys)
					obj.setId(id); // atribui esse id gerado, dentro do meu objeto obj
				}
				DB.closeResultSet(rs);
			}
			else {
				throw new DbException("Unexpected error! No rows affected!"); // como estou inserindo algo, e se nao tiver nenhuma linha afetada, entao...
			}                                                                 // alguma coisa errada acontenceu e tenho que lançar uma excessao.
			
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());	
		}	
		finally {
			DB.closeStatement(st);
		}
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
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			
			st = conn.prepareStatement("SELECT seller.*, department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "ORDER BY Name");
			
			rs = st.executeQuery();
			
			List<Seller> list = new ArrayList<Seller>();
			Map<Integer, Department> map = new HashMap<>();
			
			while (rs.next()) {
				
				Department dep = map.get(rs.getInt("DepartmentId"));
				if (dep == null) {
					
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				
				Seller obj = instantiateSeller(rs, dep);
				list.add(obj);
			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}	
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
					
					dep = instantiateDepartment(rs); // ai vou instanciar o departamento passando o rs
					map.put(rs.getInt("DepartmentId"), dep); // agora vou salvar esse departamento dentro do map, para que na prox vez, 
					// eu possa verificar que ele ja existe	
				}

				Seller obj = instantiateSeller(rs, dep); // instancio o vendedor (Seller),
				list.add(obj); // adiciono esse vendedor a list.
			}
			return list; // no final, retorno minha List.	
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
