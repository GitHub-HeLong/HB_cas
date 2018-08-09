package com.top.security.server.base.entity;

import java.util.List;

public class TreeInfo {
	/**
	 * id
	 */
	private String id;
	
	private String text;
	
	private String iconCls;
	
	private boolean lines;
	
	private boolean checked;
	
	private String state;
	
	private String parentId;
	
	private boolean doCheck = true;
	
	private Integer nodeType = 1;
	
	private boolean isParent = false;
	
	private List<TreeInfo> children;

	public TreeInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public boolean isLines() {
		return lines;
	}

	public void setLines(boolean lines) {
		this.lines = lines;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public boolean isDoCheck() {
		return doCheck;
	}

	public void setDoCheck(boolean doCheck) {
		this.doCheck = doCheck;
	}
	
	public Integer getNodeType() {
		return nodeType;
	}

	public void setNodeType(Integer nodeType) {
		this.nodeType = nodeType;
	}

	public List<TreeInfo> getChildren() {
		return children;
	}

	public void setChildren(List<TreeInfo> children) {
		this.children = children;
	}

	
	public boolean getIsParent() {
		return isParent;
	}

	public void setIsParent(boolean isParent) {
		this.isParent = isParent;
	}

	@Override
	public String toString() {
		return "TreeInfo [id=" + id + ", text=" + text + ", iconCls=" + iconCls + ", lines=" + lines + ", checked="
				+ checked + ", state=" + state + ", parentId=" + parentId + ", doCheck=" + doCheck + ", nodeType="
				+ nodeType + ", isParent=" + isParent + ", children=" + children + "]";
	}

}
