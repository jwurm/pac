package com.prodyna.academy.pac.web.controller;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

@ManagedBean(name = "navigationController", eager = true)
@RequestScoped
public class NavigationController implements Serializable {

	// this managed property will read value from request parameter pageId
	@ManagedProperty(value = "#{param.pageId}")
	private String pageId;

	public void setPageId(String pageId) {
		this.pageId = pageId;
	}

	public String getPageId() {
		return pageId;
	}

	// condional navigation based on pageId
	// if pageId is 1 show page1.xhtml,
	// if pageId is 2 show page2.xhtml
	// else show home.xhtml
	public String showPage() {
		if (pageId == null) {
			return "home";
		}
		if (pageId.equals("1")) {
			return "page";
		} else if (pageId.equals("2")) {
			return "page2";
		} else {
			return "home";
		}
	}
}
