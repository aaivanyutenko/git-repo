<%@page import="by.gsu.mathan.data.OwnConstants"%>
<%@page import="by.gsu.mathan.data.CoursesManager"%>
<%@page import="by.gsu.mathan.beans.Course"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Гомельский государственный университет имени Франциска Скорины</title>
<link rel="stylesheet" type="text/css" href="http://ext.by/resources/css/ext-all.css" />
<script type="text/javascript" src="http://ext.by/bootstrap.js"></script>
<script type="text/javascript" src="http://mathjax.by/MathJax.js?config=MML_HTMLorMML"></script>
<%Course course = CoursesManager.getCourseById(request.getParameter("courseId"));%>
<script type="text/javascript">
	Ext.onReady(function() {
		var treeStore = Ext.create('Ext.data.TreeStore', {
			proxy: {
				type: 'ajax',
				url: 'get-course-content?course_id=<%=course.getId()%>'
			}
		});
		
		var addItemWindow = null;
		addItemWindow = Ext.create('Ext.Window', {
			width: 400,
			closable: false,
			items: [{
				xtype: 'form',
				bodyPadding: 10,
				frame: true,
				fieldDefaults: {
					labelWidth: 150
				},
				items: [{
					xtype: 'displayfield',
					id: 'parent-name',
					fieldLabel: 'Родительский элемент'
				}, {
					xtype: 'filefield',
					id: 'docxFile',
					fieldLabel: 'Укажите файл',
					buttonText: 'Обзор'
				}, {
					xtype: 'hidden',
					name: 'courseId',
					value: '<%=course.getId()%>'
				}],
				buttons: [{
					xtype: 'button',
					text: 'Добавить',
					handler: function() {
						var form = this.up('form').getForm();
						if (form.isValid()) {
							form.submit({
								params: {
									parentId: Ext.getCmp('tree').getSelectionModel().getSelection()[0].raw.id
								},
								url: 'add-item',
								waitMsg: 'Загрузка...',
								success: function(fp, o) {
									treeStore.load();
								}
							});
						}
						addItemWindow.hide();
					}
				}, {
					xtype: 'button',
					text: 'Отмена',
					handler: function() {
						addItemWindow.hide();
					}
				}]
			}]
		});
		
		var treePanelBbar = null;
		<%if (session.getAttribute("user") != null) {%>
		treePanelBbar = [{
			xtype: 'button',
			text: 'Добавить',
			handler: function() {
				var parent = Ext.getCmp('tree').getSelectionModel().getSelection()[0];
				if (parent != null) {
					parent = parent.raw;
					var parentField = Ext.getCmp('parent-name');
					parentField.setValue(parent.text);
					addItemWindow.show();
				} else {
					Ext.Msg.alert('Внимание!', 'Сначала выберите в дереве родительский элемент.');
				}
			}
		}, {
			xtype: 'button',
			text: 'Удалить',
			handler: function() {
				var node = Ext.getCmp('tree').getSelectionModel().getSelection()[0];
				if (node != null) {
					node = node.raw;
					var msg = 'Вы действительно хотите удалить узел «' + node.text + '»?';
					
					if (node.expanded) {
						msg = 'Узел «' + node.text + '» не пустой. Вы действительно хотите удалить этот узел?';
					}
					
					Ext.Msg.confirm('Внимание!', msg, function(btn) {
						if (btn == 'yes'){
							Ext.Ajax.request({
								url: 'delete-item',
								params: {
									itemId: node.id,
									courseId: '<%=course.getId()%>'
								},
								success: function(response) {
									treeStore.load();
								}
							});
						}
					});
				} else {
					Ext.Msg.alert('Внимание!', 'Сначала выберите в дереве элемент для удаления.');
				}
			}
		}];
		<%}%>

		var treePanel = Ext.create('Ext.tree.Panel', {
			region: 'north',
			id: 'tree',
			border: 0,
			bodyStyle: {
				'border-top-width': 0
			},
			store: treeStore,
			rootVisible: false,
			height: '100%',
			bbar: treePanelBbar,
			listeners: {
				selectionchange: function(model, records) {
					if (records[0] != null) {
						var iframe = document.getElementById('dynamic-loaded-frame');
						if (records[0].raw.id != <%="'" + OwnConstants.DEFINITIONS_ROOT_ID + "'"%> && records[0].raw.id != <%="'" + OwnConstants.THEOREMS_ROOT_ID + "'"%>) {
							iframe.src = 'get-item?course_id=<%=course.getId()%>&item_id=' + records[0].raw.id;
						}
					}
				}
			}
		});
		
		Ext.create('Ext.Viewport', {
			layout: 'border',
			items: [ {
				xtype: 'box',
				id: 'header',
				region: 'north',
				margins: '2 0 2 5',
				html: '<h1><%=course.getName()%></h1>'
			}, {
				region: 'west',
				margins: '2 0 5 5',
				bodyStyle: {
					'border-right-width': 0
				},
				width: 475,
				autoScroll: true,
				items: [ treePanel ]
			}, {
				id: 'content-panel',
				region: 'center',
				html: '<iframe id="dynamic-loaded-frame" width="100%" height="100%" style="border: 0px;"></iframe>',
				margins: '2 5 5 0'
			}, {
				region: 'south',
				xtype: 'toolbar',
				items: [{
					xtype: 'button',
					text: 'Назад',
					handler: function() {
						location.replace('index.jsp');
					}
				}]
			} ],
			renderTo: Ext.getBody()
		});
	});
</script>
</head>
<body>
</body>
</html>