package com.scp.demo.HibernateMethods;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Order;

public class StartORM {
	public static SessionFactory sfactory = new Configuration().
			configure("mysql.cfg.xml").buildSessionFactory();

	
	private static boolean addEmployee(Employee emp) {
		
		Employee dbEmp = getEmployee(emp.getEmpId());
		if(dbEmp!=null) {
			System.out.println("Cannot add since already exist..." +emp.getEmpId());
			return false;
		}
		
		Session session = sfactory.openSession();
		Transaction tr =session.beginTransaction();
		try {
			session.save(emp);
			System.out.println("Emp Saved Successfully.....!");
			return true;
		}catch(Exception e) {
			System.out.println("Problem in employee insertion..!");
			e.printStackTrace();
			return false;
		}finally {
			closeResources(tr,session);
		}
	}

	private static void closeResources(Transaction tr, Session session) {
		
		if(session!=null) {
			session.flush();
			if(tr!=null) {
				tr.commit();
			}
			session.close();
		}
		
	}

	private static boolean deleteEmp(int empId) {
		Employee dbEmp = getEmployee(empId);
		if(dbEmp!=null) {
			Session session = sfactory.openSession();
			Transaction tr =session.beginTransaction();
			try {
				session.delete(dbEmp);
				System.out.println("Emp deleted Successfully.....!");
				return true;
			}catch(Exception e) {
				System.out.println("Problem in employee deletion..!");
				e.printStackTrace();
			}finally {
				closeResources(tr,session);
			}
		}else {
			System.out.println("Emp with given identifer not exist so cannot delete..!");
		}
		
		return false;
	}

	private static Employee updateEmployee(Employee emp) {
		Employee dbEmployee  = getEmployee(emp.getEmpId());
		if(dbEmployee!=null) {
			Session session = sfactory.openSession();
			Transaction tr =session.beginTransaction();
			try {
				dbEmployee.setEmpAge(emp.getEmpAge());
				dbEmployee.setEmpName(emp.getEmpName());
				session.update(dbEmployee);
				System.out.println("Emp updated Successfully.....!");
			}catch(Exception e) {
				System.out.println("Problem in employee insertion..!");
				e.printStackTrace();
			}finally {
				closeResources(tr,session);
			}
		}else {
			System.out.println("Given empid not exist..so cannot update...!");
		}
		return getEmployee(emp.getEmpId());
	}

	private static Employee getEmployee(int empId) {
		return sfactory.openSession().get(Employee.class, empId);
	}


	private static List<Employee> getAllEmployees() {
		return sfactory.openSession().createCriteria(Employee.class).list();
	}

	private static List<Employee> getOrderedCollection() {
		
		Session session = sfactory.openSession();
		//return session.createSQLQuery("select * from employee_info order by emp_name").addEntity(Employee.class).list(); //SQL
		//return session.createQuery("from Employee order by empName desc").list(); //HQL
		return session.createCriteria(Employee.class).addOrder(Order.desc("empName")).list();
		
		//sql -- db -- table- --col -- addentity -- list -- dependent
		//hsql -- orm  -- class - field -- list() -- indepdenent
		
		
	}
		
	private static List<Employee> getSortedCollection() {
		Comparator<Employee> ageDesc = new Comparator<Employee>() {
			@Override
			public int compare(Employee o1, Employee o2) {
				return -(o1.getEmpAge() - o2.getEmpAge());
			}
		};
		List<Employee> emps = getAllEmployees();
		Collections.sort(emps,ageDesc);
		return emps;
	}
	
	
	public static void main(String[] args) {
		
		  Employee e1 =new Employee(11,"AFAA",33); Employee e2 =new
		  Employee(22,"VVAA",44); Employee e3 =new Employee(33,"BBAA",25);
		  addEmployee(e1); addEmployee(e2); addEmployee(e3);
		 
		
		System.out.println("Sorted collection...." +getOrderedCollection());
		//System.out.println("Sorted collection...." +getSortedCollection());
		//System.out.println(deleteEmp(2));
		
		
		//System.out.println(getAllEmployees());
		/*
		
			Employee e1 =new Employee(1,"ZZZZZ",33);
		System.out.println(updateEmployee(e1));
		
		
			Employee e1 =new Employee(8,"AFAA",33);
			Employee e2 =new Employee(9,"VVAA",44);
			Employee e3 =new Employee(5,"BBAA",25);
			addEmployee(e1);
			addEmployee(e2);
			addEmployee(e3);
		*/
			
			
			
	}

}
