<%@page import="by.gsu.mathan.data.CoursesManager"%>
<%@page import="by.gsu.mathan.beans.Course"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Гомельский государственный университет имени Франциска Скорины</title>
<link rel="stylesheet" type="text/css" href="http://<%=request.getServerName() + ":" + request.getServerPort()%>/ext-container/resources/css/ext-all.css" />
<script type="text/javascript" src="http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=MML_HTMLorMML"></script>
<script type="text/javascript" src="http://<%=request.getServerName() + ":" + request.getServerPort()%>/ext-container/bootstrap.js"></script>
<%Course course = CoursesManager.getCourseById(request.getParameter("courseId"));%>
<script type="text/javascript">
	Ext.onReady(function() {
		var treeStore = Ext.create('Ext.data.TreeStore', {
			proxy : {
				type : 'ajax',
				url : 'get-course-content?course_id=<%=course.getId()%>'
			}
		});
		
		var addItemWindow = Ext.create('Ext.Window', {
			width: 400,
			closable : false,
			items : [{
				xtype : 'form',
				bodyPadding : 10,
				frame : true,
				fieldDefaults : {
					labelWidth : 150
				},
				items : [{
					xtype : 'displayfield',
					id : 'parent-id',
					fieldLabel : 'Родительский элемент'
				}, {
					xtype : 'filefield',
					id : 'docxFile',
					fieldLabel : 'Укажите файл',
					buttonText : 'Обзор'
				}, {
					xtype : 'hidden',
					name : 'courseId',
					value : '<%=course.getId()%>'
				}],
				buttons : [{
					xtype : 'button',
					text : 'Добавить',
					handler : function() {
						var fileField = Ext.getCmp('docxFile');
						var form = this.up('form').getForm();
						if (form.isValid()) {
							form.submit({
								params : {
									parentId : Ext.getCmp('tree').getSelectionModel().getSelection()[0].raw.id
								},
								url : 'add-item',
								waitMsg : 'Загрузка...',
								success : function(fp, o) {
								}
							});
						}
						addItemWindow.hide();
					}
				}, {
					xtype : 'button',
					text : 'Отмена',
					handler : function() {
						addItemWindow.hide();
					}
				}]
			}]
		});

		var treePanel = Ext.create('Ext.tree.Panel', {
			region : 'north',
			id : 'tree',
			rootVisible : false,
			store : treeStore,
			bbar : [<%if (session.getAttribute("user") != null) {%>{
				xtype : 'button',
				text : 'Добавить',
				handler : function() {
					var parent = Ext.getCmp('tree').getSelectionModel().getSelection()[0];
					if (parent != null) {
						var parentField = Ext.getCmp('parent-id');
						parentField.setValue(parent.raw.text);
						addItemWindow.show();
					} else {
						Ext.Msg.alert('Внимание!', 'Сначала выберите в дереве родительский элемент.');
					}
				}
			}, {
				xtype : 'button',
				text : 'Удалить',
				handler : function() {
				}
			}<%}%>],
			listeners : {
				selectionchange : function(model, records) {
					if (records[0] != null) {
						var iframe = document.getElementById('dynamic-loaded-frame');
						iframe.src = 'get-item?course_id=<%=course.getId()%>&item_id=' + records[0].raw.id;
					}
				}
			}
		});
		
		Ext.create('Ext.Viewport', {
			layout : 'border',
			items : [ {
				xtype : 'box',
				id : 'header',
				region : 'north',
				html : '<h1><%=course.getName()%></h1>'
			}, {
				id : 'layout-border',
				region : 'west',
				margins : '2 0 5 5',
				width : 475,
				autoScroll : true,
				items : [ treePanel ]
			}, {
				id : 'content-panel',
				region : 'center',
				html: '<iframe id="dynamic-loaded-frame" width="100%" height="100%" style="border: 0px;"></iframe>',
				margins : '2 5 5 0'
			}, {
				region : 'south',
				xtype : 'toolbar',
				items : [{
					xtype : 'button',
					text : 'Назад',
					handler : function() {
						location.replace('index.jsp');
					}
				}]
			} ],
			renderTo : Ext.getBody()
		});
	});
</script>
</head>
<body>
	<form id="course-form" action="course.jsp" method="post">
		<input id="input" type="hidden" name="courseId">
	</form>
</body>
</html>