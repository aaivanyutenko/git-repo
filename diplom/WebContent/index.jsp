<%@page import="by.gsu.mathan.data.OwnConstants"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Гомельский государственный университет имени Франциска Скорины</title>
<link rel="stylesheet" type="text/css" href="http://dev.sencha.com/deploy/ext-4.0.2a/resources/css/ext-all.css" />
<script type="text/javascript" src="http://dev.sencha.com/deploy/ext-4.0.2a/bootstrap.js"></script>
<script type="text/javascript">
	var user = <%=session.getAttribute("user")%>;
	
	Ext.onReady(function() {
		var addCourseWindow = null;
		addCourseWindow = Ext.create('Ext.Window', {
			title : 'Создание нового курса',
			closable : false,
			width : 400,
			items : [{
				xtype : 'form',
				id : 'courseNameForm',
				bodyPadding : 10,
				frame : true,
				items : [{
					xtype: 'textfield',
					id : 'courseName',
					fieldLabel : 'Название курса',
					name : 'courseName',
					allowBlank : false
				}],
				buttons : [ {
					xtype : 'button',
					text : 'Добавить',
					handler : function() {
						var items = store.data.items;
						var add = true;
						var courseName = Ext.getCmp('courseName').value;
						for (var i = 0; i < items.length; i++) {
							var itemCourseName = items[i].data.name;
							if (itemCourseName == courseName) {
								add = false;
								alert('Курс с таким названием уже существует');
								break;
							}
						}
						if (add) {
							addCourseWindow.hide();
							this.up('form').getForm().reset();
							Ext.Ajax.request({
								url : 'add-course',
								params : {
									courseName : courseName
								},
								success : function(response) {
									store.load();
								}
							});
						}
					}
				}, {
					xtype : 'button',
					text : 'Отмена',
					handler : function() {
						addCourseWindow.hide();
					}
				}]
			}]
		});
		
		var store = Ext.create('Ext.data.ArrayStore', {
			autoLoad : true,
			fields : [ {
				name : 'name',
				type : 'string',
			}, {
				name : 'id'
			} ],
			proxy : {
				type : 'ajax',
				url : 'get-courses',
				reader : {
					type : 'array'
				}
			}
		});
		
		/*Ext.create('Ext.grid.Panel', {
			bbar : {
				xtype: 'form',
				url: 'login',
				padding: '6 0 0 7',
				height: 38,
				frame: true,
				items: [{
					xtype: 'container',
					anchor: '100%',
					layout:'column',
					defaults: {
						xtype: 'container',
						layout: 'anchor'
					},
					items:[
						<%if (session.getAttribute("user") == null) {%>{
							columnWidth: .1,
							items: [{
								xtype : 'textfield',
								name : 'username',
								emptyText : 'логин'
							}]
						}, {
							columnWidth: .1,
							items: [{
								xtype : 'textfield',
								name : 'password',
								inputType : 'password',
								emptyText : 'пароль'
							}]
						}, {
							columnWidth: .1,
							items: [{
								xtype : 'button',
								text : 'Авторизация',
								handler : function() {
									var form = this.up('form').getForm();
									
									form.submit({
										success : function(response) {
											location.reload();
										}
									});
								}
							}]
						}<%} else {%>{
							columnWidth: .06,
							items: [{
								xtype : 'button',
								text : 'Добавить курс',
								handler : function() {
									addCourseWindow.show();
								}
							}]
						},{
							columnWidth: .08,
							items: [{
								xtype : 'button',
								text : 'Редактировать курс',
								handler : function() {
									var form = document.getElementById('course');
									form.input.value = Ext.getCmp('courses-grid').getSelectionModel().getSelection()[0].data.id;
									form.submit();
								}
							}]
						},{
							columnWidth: .057,
							items: [{
								xtype : 'button',
								text : 'Удалить курс',
								handler : function() {
									var courseId = Ext.getCmp('courses-grid').getSelectionModel().getSelection()[0].data.id;
									Ext.Ajax.request({
										url : 'delete-course',
										params : {
											courseId : courseId
										},
										success : function(response) {
											store.load();
										}
									});
								}
							}]
						},{
							columnWidth: .1,
							items: [{
								xtype : 'button',
								text : 'Выход',
								handler : function() {
									Ext.Ajax.request({
										url : 'logout',
										success : function(response) {
											location.replace('index.jsp');
										}
									});
								}
							}]
						}<%}%>]
				}]
			},
			listeners : {
				itemclick : function(view, record, item, index, event) {
					<%if (session.getAttribute("user") == null) {%>
						var form = document.getElementById('course');
						form.input.value = record.data.id;
						form.submit();
					<%}%>
				}
			}
		});*/
		
		var mainWindow = Ext.create('Ext.window.Window', {
			closable: false,
			resizable: false,
			draggable: false,
			width: 600,
			height: 400,
			layout: 'fit',
			
			items: [{
				xtype: 'grid',
				store : store,
				enableColumnResize: false,
				columns : [{
					text : 'Выберите курс:',
					menuDisabled : true,
					flex: 1,
					dataIndex : 'name'
				}]
				<%if (session.getAttribute("user") == null) {%>,
				listeners : {
					itemclick : function(view, record, item, index, event) {
						var form = document.getElementById('course');
						form.input.value = record.data.id;
						form.submit();
					}
				}
				<%}%>
			}],
			buttons: [{
				text: 'Авторизация',
				handler: function() {
					authorizeWindow.show();
				}
			}]
		});
		
		mainWindow.show();
		
		var authorizeWindow = null;
		authorizeWindow = Ext.create('Ext.window.Window', {
			width: 350,
			height: 130,
			layout: 'fit',
			resizable: false,
			closable: false,
			
			items: [{
				xtype: 'form',
				border: false,
				url: 'login',
				defaultType: 'textfield',
				bodyPadding: 5,
				
				fieldDefaults: {
					labelWidth: 125
				},
				
				items: [{
					fieldLabel: 'Имя пользователя',
					name: '<%=OwnConstants.USER_NAME%>',
					anchor: '100%'
				}, {
					inputType: 'password',
					fieldLabel: 'Пароль',
					name: '<%=OwnConstants.PASSWORD%>',
					anchor: '100%'
				}]
			}],
			
			buttons: [{
				text: 'Вход',
				handler: function() {
					var form = this.up('window').down('form');
					
					form.submit({
						success: function(response) {
							location.reload();
						}
					});
				}
			}, {
				text: 'Отмена',
				handler: function() {
					authorizeWindow.hide();
				}
			}]
		});
	});
</script>
</head>
<body>
	<form id="course" action="course.jsp" method="post">
		<input id="input" type="hidden" name="courseId">
	</form>
	<div id="courses-grid"></div>
</body>
</html>