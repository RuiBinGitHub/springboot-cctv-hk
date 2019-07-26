package com.springboot.biz.impl;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.springboot.biz.CompanyBiz;
import com.springboot.biz.PersonBiz;
import com.springboot.dao.CompanyDao;
import com.springboot.entity.Company;
import com.springboot.entity.Person;
import com.springboot.util.AppUtils;

@Transactional
@Service(value = "companyBiz")
public class CompanyBizImpl implements CompanyBiz {

	@Resource
	private CompanyDao companyDao;
	@Resource
	private PersonBiz personBiz;

	private Map<String, Object> map = null;

	/** 插入数据 */
	public void insertCompany(Company company) {
		companyDao.insertCompany(company);
	}

	/** 更新数据 */
	public void updateCompany(Company company) {
		companyDao.updateCompany(company);
	}

	/** 删除数据 */
	public void deleteCompany(Company company) {
		companyDao.deleteCompany(company);
	}

	public Company findInfoCompany(int id) {
		map = AppUtils.getMap("id", id);
		return companyDao.findInfoCompany(map);
	}

	/** 获取company对象 */
	public Company findInfoCompany(Map<String, Object> map) {
		return companyDao.findInfoCompany(map);
	}

	/** 获取company列表 */
	public List<Company> findListCompany(Map<String, Object> map) {
		return companyDao.findListCompany(map);
	}

	public int getCountPage(Map<String, Object> map, int size) {
		if (!StringUtils.isEmpty(map.get("name")))
			map.put("name", "%" + map.get("name") + "%");
		int count = companyDao.getCount(map);
		return (int) Math.ceil((double) count / size);
	}

	public void appendCompany(Company company) {
		String date = AppUtils.getDate(null);
		Format foramt1 = new DecimalFormat("#0000");
		company.setCode(AppUtils.findCode());
		company.setDate(date);
		companyDao.insertCompany(company);
		for (int i = 0; i < company.getCont(); i++) {
			Person person = new Person();
			String role = i == 0 ? "Role2" : "Role4";
			String name = foramt1.format(i + 1);
			person.setNickname("No：" + name);
			person.setUsername(company.getDefine() + name);
			person.setPassword(AppUtils.findPass());
			person.setPhone("--");
			person.setRole(role);
			person.setDate(date);
			person.setState("1");
			person.setCompany(company);
			personBiz.insertPerson(person);
		}
	}

	public void repeatCompany(Company company) {
		Company temp = findInfoCompany(company.getId());
		company.setDefine(temp.getDefine());
		company.setDate(temp.getDate());
		this.updateCompany(company);
		if (company.getCont() < temp.getCont()) {
			map = AppUtils.getMap("company", company);
			List<Person> persons = personBiz.findListPerson(map);
			for (int i = persons.size(); i > company.getCont(); i--) {
				persons.get(i - 1).setState("0");
				personBiz.updatePerson(persons.get(i - 1));
			}
		} else if (company.getCont() > temp.getCont()) {
			map = AppUtils.getMap("state", "0", "company", company);
			List<Person> persons = personBiz.findListPerson(map);
			System.out.println(persons.size());
			int count = company.getCont() - temp.getCont();
			for (int i = 0; i < count && i < persons.size(); i++) {
				persons.get(i).setState("1");
				personBiz.updatePerson(persons.get(i));
			}
			String date = AppUtils.getDate(null);
			Format foramt1 = new DecimalFormat("#0000");
			map = AppUtils.getMap("company", company);
			count = personBiz.getPageCount(map, 1);
			for (int i = count; i < company.getCont(); i++) {
				Person person = new Person();
				String name = foramt1.format(i + 1);
				person.setNickname("No：" + name);
				person.setUsername(company.getDefine() + name);
				person.setPassword(AppUtils.findPass());
				person.setPhone("--");
				person.setRole("Role4");
				person.setDate(date);
				person.setState("1");
				person.setCompany(company);
				personBiz.insertPerson(person);
			}
		}
	}
}
