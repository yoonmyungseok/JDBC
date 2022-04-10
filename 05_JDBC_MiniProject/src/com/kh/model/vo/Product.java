package com.kh.model.vo;

public class Product {
	
	private String productId;	    //PRODUCT_ID VARCHAR2(20) PRIMARY KEY,
	private String productName;	    //PRODUCT_NAME VARCHAR2(30) NOT NULL,
	private int price;	    		//PRICE NUMBER NOT NULL,
	private String description;	    //DESCRIPTION VARCHAR2(300),
	private int stock;	    		//STOCK NUMBER NOT NULL
	private int category_no; 		// Category NUMBER not null
	private String deleteYn;        // DELETE_YN VARCHAR2(1) DEFAULT 'N' CHECK(DELETE_YN IN ('Y', 'N'))
	
	private String category_name; 	//카테고리 명(String타입)
	
	public Product() {
		super();
	}
	
	public Product(String productId, String productName, int price, String description, int stock, int category_no) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.price = price;
		this.description = description;
		this.stock = stock;
		this.category_no = category_no;
	}

	
	@Override
	public String toString() {
		return "상품 아이디 : " + productId + ", 상품명 : " + productName + ", 상품 가격 : " + price
				+ ", 상품 상세 정보 : " + description + ", 상품 재고 : " + stock + ", 카테고리 : " + category_name;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}


	public int getCategory_no() {
		return category_no;
	}

	public void setCategory_no(int category_no) {
		this.category_no = category_no;
	}

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

	public String getDeleteYn() {
		return deleteYn;
	}

	public void setDeleteYn(String deleteYn) {
		this.deleteYn = deleteYn;
	}
}
