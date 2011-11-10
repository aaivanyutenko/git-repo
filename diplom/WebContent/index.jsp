<%@page import="by.gsu.mathan.data.OwnConstants"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Гомельский государственный университет имени Франциска Скорины</title>
<link rel="stylesheet" type="text/css" href="http://cdn.sencha.io/ext-4.0.7-gpl/resources/css/ext-all.css" />
<script type="text/javascript" src="http://cdn.sencha.io/ext-4.0.7-gpl/bootstrap.js"></script>
<script type="text/javascript">
	var user = <%=session.getAttribute("user")%>;
	
	Ext.require([
		'Ext.window.Window',
		'Ext.data.ArrayStore'
	]);
	
	Ext.onReady(function() {
		var addCourseWindow = null;
		addCourseWindow = Ext.create('Ext.window.Window', {
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
		
		var gridListeners = null;
		var mainWindowButtons = null;
		<%if (session.getAttribute("user") == null) {%>
		gridListeners = {
			itemclick : function(view, record, item, index, event) {
				var form = document.getElementById('course');
				form.input.value = record.data.id;
				form.submit();
			}
		};
		mainWindowButtons = [{
			text: 'Авторизация',
			handler: function() {
				authorizeWindow.show();
			}
		}];
		<%} else {%>
		mainWindowButtons = [{
			text : 'Добавить курс',
			handler : function() {
				addCourseWindow.show();
			}
		}, {
			text : 'Редактировать курс',
			handler : function() {
				var id = Ext.getCmp('course-grid').getSelectionModel().getSelection()[0].data.id;
				location.replace('course.jsp?courseId=' + id);
			}
		}, {
			text : 'Удалить курс',
			handler : function() {
				var course = Ext.getCmp('course-grid').getSelectionModel().getSelection()[0].data;
				Ext.Msg.confirm('Внимание!', 'Вы действительно хотите удалить курс ' + course.name, function(btn) {
					if (btn == 'yes'){
						var courseId = course.id;
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
				});
			}
		}, {
			text : 'Выход',
			handler : function() {
				Ext.Ajax.request({
					url : 'logout',
					success : function(response) {
						location.replace('index.jsp');
					}
				});
			}
		}];
		<%}%>
		
		var mainWindow = Ext.create('Ext.window.Window', {
			closable: false,
			resizable: false,
			draggable: false,
			width: 600,
			height: 400,
			bodyStyle: {
				'border': 0
			},
			layout: 'fit',
			
			items: [{
				xtype: 'grid',
				id: 'course-grid',
				store : store,
				enableColumnResize: false,
				columns : [{
					text : 'Выберите курс:',
					menuDisabled : true,
					flex: 1,
					dataIndex : 'name'
				}],
				listeners : gridListeners
			}],
			buttons: mainWindowButtons
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