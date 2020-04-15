package servlet.provider;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import com.alibaba.fastjson.JSONArray;
import com.mysql.cj.util.StringUtils;
import pojo.Provider;
import pojo.User;
import service.provider.ProviderService;
import service.provider.ProviderServiceImpl;
import util.Constants;


public class ProviderServlet extends HttpServlet {



	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	enum EnumMethod{
		query,add,view,modify,
		modifysave,delprovider;

	}
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		EnumMethod method = EnumMethod.valueOf(EnumMethod.class,request.getParameter("method"));
		switch (method){
			case modify:
				getProviderById(request,response,"providermodify.jsp");
				break;
			case query:
				query(request,response);
				break;
			case view:
				getProviderById(request,response,"providerview.jsp");
				break;
			case add:
				add(request,response);
				break;
			case modifysave:
				modify(request,response);
				break;
			case delprovider:
				delProvider(request,response);
				break;
		}
	}
	
	private void delProvider(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String id = request.getParameter("proid");
		HashMap<String, String> resultMap = new HashMap<String, String>();
		if(!StringUtils.isNullOrEmpty(id)){
			ProviderService providerService = new ProviderServiceImpl();
			int flag = providerService.deleteProviderById(id);
			if(flag == 0){//删除成功
				Constants.UPDATE_Count++;
				resultMap.put("delResult", "true");
			}else if(flag == -1){//删除失败
				resultMap.put("delResult", "false");
			}else if(flag > 0){//该供应商下有订单，不能删除，返回订单数
				resultMap.put("delResult", String.valueOf(flag));
			}
		}else{
			resultMap.put("delResult", "notexit");
		}
		//把resultMap转换成json对象输出
		response.setContentType("application/json");
		PrintWriter outPrintWriter = response.getWriter();
		outPrintWriter.write(JSONArray.toJSONString(resultMap));
		outPrintWriter.flush();
		outPrintWriter.close();
	}
	
	private void modify(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String proContact = request.getParameter("proContact");
		String proPhone = request.getParameter("proPhone");
		String proAddress = request.getParameter("proAddress");
		String proFax = request.getParameter("proFax");
		String proDesc = request.getParameter("proDesc");
		String id = request.getParameter("id");
		Provider provider = new Provider();
		provider.setId(Integer.valueOf(id));
		provider.setProContact(proContact);
		provider.setProPhone(proPhone);
		provider.setProFax(proFax);
		provider.setProAddress(proAddress);
		provider.setProDesc(proDesc);
		provider.setModifyBy(((User)request.getSession().getAttribute(Constants.USER_SESSION)).getId());
		provider.setModifyDate(new Date());
		boolean flag = false;
		ProviderService providerService = new ProviderServiceImpl();
		flag = providerService.modify(provider);
		if(flag){
			Constants.UPDATE_Count++;
			response.sendRedirect(request.getContextPath()+"/jsp/provider.do?method=query");
		}else{
			request.getRequestDispatcher("providermodify.jsp").forward(request, response);
		}
	}
	
	private void getProviderById(HttpServletRequest request, HttpServletResponse response,String url)
			throws ServletException, IOException {
		String id = request.getParameter("proid");
		if(!StringUtils.isNullOrEmpty(id)){
			ProviderService providerService = new ProviderServiceImpl();
			Provider provider = null;
			provider = providerService.getProviderById(id);
			request.setAttribute("provider", provider);
			request.getRequestDispatcher(url).forward(request, response);
		}
	}
	private void add(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String proCode = request.getParameter("proCode");
		String proName = request.getParameter("proName");
		String proContact = request.getParameter("proContact");
		String proPhone = request.getParameter("proPhone");
		String proAddress = request.getParameter("proAddress");
		String proFax = request.getParameter("proFax");
		String proDesc = request.getParameter("proDesc");
		
		Provider provider = new Provider();
		provider.setProCode(proCode);
		provider.setProName(proName);
		provider.setProContact(proContact);
		provider.setProPhone(proPhone);
		provider.setProFax(proFax);
		provider.setProAddress(proAddress);
		provider.setProDesc(proDesc);
		provider.setCreatedBy(((User)request.getSession().getAttribute(Constants.USER_SESSION)).getId());
		provider.setCreationDate(new Date());
		boolean flag = false;
		ProviderService providerService = new ProviderServiceImpl();
		flag = providerService.add(provider);
		if(flag){
			Constants.UPDATE_Count++;
			response.sendRedirect(request.getContextPath()+"/jsp/provider.do?method=query");
		}else{
			request.getRequestDispatcher("provideradd.jsp").forward(request, response);
		}
	}
	
	private void query(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String queryProName = request.getParameter("queryProName");
		String queryProCode = request.getParameter("queryProCode");
		if(StringUtils.isNullOrEmpty(queryProName)){
			queryProName = "";
		}
		if(StringUtils.isNullOrEmpty(queryProCode)){
			queryProCode = "";
		}
		List<Provider> providerList = new ArrayList<Provider>();
		ProviderService providerService = new ProviderServiceImpl();
		providerList = providerService.getProviderList(queryProName,queryProCode);
		request.setAttribute("providerList", providerList);
		request.setAttribute("queryProName", queryProName);
		request.setAttribute("queryProCode", queryProCode);
		request.getRequestDispatcher("providerlist.jsp").forward(request, response);
	}
	
	


}
