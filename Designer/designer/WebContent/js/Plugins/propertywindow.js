/**
 * Copyright (c) 2006
 * Martin Czuchra, Nicolas Peters, Daniel Polak, Willi Tscheschner
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 **/


if(!ORYX.Plugins) {
	ORYX.Plugins = new Object();
}

if (!ORYX.FieldEditors) {
	ORYX.FieldEditors = {};
}

if (!ORYX.LabelProviders) {
    ORYX.LabelProviders = {};
}

// Plugin modifications to show properties in a new window (not in the right panel)
var win;
var progressDialog = null;
var filetoUploadName = "";
var sparkName = "";
var dirWiki = "http://www.sparkingtogether.com/wiki/index.php?title=";
var printable = "&amp;printable=yes";
var pandoraServlet = "/designer/PandoraServlet";
var loadTemplateServlet = "/designer/LoadEditorTemplateServlet";
var aimlTemplate = "sample.aiml";
var scriptTemplate = "script.js";

ORYX.Plugins.PropertyWindow = {

	facade: undefined,

	construct: function(facade) {
		// Reference to the Editor-Interface
		this.facade = facade;
		
		// Extended by Ana Paula (start)
		progressDialog = null;    
		// Extended by Ana Paula (end)

		// Plugin modifications to show properties in a new window (not in the right panel)
		// Commented "ORYX.CONFIG.EVENT_SHOW_PROPERTYWINDOW" and "ORYX.CONFIG.EVENT_LOADED"
		//this.facade.registerOnEvent(ORYX.CONFIG.EVENT_SHOW_PROPERTYWINDOW, this.init.bind(this));		
		this.facade.registerOnEvent(ORYX.CONFIG.EVENT_DBLCLICK, function(event, shape){this.actonDBLClick(event, shape);}.bind(this));
		// FIX BUG that avoid selection without doubleclick some shapes first.
		this.shapeSelection.shapes = [this.facade.getCanvas()];
		//this.facade.registerOnEvent(ORYX.CONFIG.EVENT_LOADED, this.selectDiagram.bind(this));		
		this.init();
	},
	
	// Plugin modifications to show properties in a new window (not in the right panel)
	actonDBLClick: function actonDBLClick(event, shape){
		// If we are not working with a shape
		// we ignore the doble-click event
		if( !(shape instanceof ORYX.Core.Shape) ){ 
			return; 
		} 
		// If we are not working with a shape which belongs to "Components" group
		// we ignore the doble-click event
		if(shape.getStencil().groups().first() != "Components"){
			return;
		}
		//alert("Registrando evento dblClick on a shape");
		if(win)
			win.close();
		
		// inicializamos las propiedades
		this.init();	
		
		// Assign the current selections
		this.shapeSelection.shapes = [shape];
		
		// Check if current selection is PandorabotsSpark and add the 'Most active' button		
		if(shape.getStencil().id().indexOf("Pandora") != -1){
			win = new Ext.Window({
				width: 440,		
				height: 210,
				layout: "fit",
				border: false,				
				buttonAlign: 'center',
				buttons: [
				          {
				        	  xtype:'button',	
				        	  minWidth: 100,
				        	  text:ORYX.I18N.PropertyWindow.helpButton,
				        	  listeners:{                                        
				        		  click: function(){
				        			  var iframeContainer = new Ext.BoxComponent({
				        				  autoEl: {
				        					  tag: 'iframe',
				        					  frameborder: '0',
				        					  src: dirWiki+sparkName+printable
				        				  },
				        				  listeners: {
				        					  afterrender: function () {
				        						  console.log('rendered');

				        						  this.getEl().on('load', function () {
				        							  console.log('loaded');
				        						  });
				        					  }
				        				  }
				        			  });
				        			  new Ext.Window({
				        				  title: 'Spark Help',
				        				  layout:'fit',
				        				  renderTo: Ext.getBody(),
				        				  floating: true,						        	    
				        				  width: 800,
				        				  height: 500,						        	    
				        				  items:[
				        				         iframeContainer
				        				         ]
				        			  }).show();
				        		  }
				        	  }                                        
				          },
				          {
				        	  xtype:'button',						    
				        	  text: 'Most active',
				        	  minWidth: 100,
				        	  listeners:{                                        
				        		  click: function(){
				        			  new Ext.Window({
				        				  title: 'Most Popular Pandorabots for Last 24 Hours',
				        				  width: 300,
				        				  height: 500,				
				        				  autoScroll: true,
				        				  autoLoad: {
				        					  url: pandoraServlet						        			
				        				  }
				        			  }).show();				        	
				        		  }
				        	  }                                        
				          }
				          ],
				items: [
				        this.grid
				       ]
			});		
		}else{
			win = new Ext.Window({				
				width: 440,		
				height: 210,
				layout: "fit",
				border: false,				
				buttonAlign: 'center',					        
				buttons: [
				          {
				        	  xtype:'button',				        	  
				        	  text:ORYX.I18N.PropertyWindow.helpButton,
				        	  listeners:{                                        
				        		  click: function(){				        			  
				        			  var iframeContainer = new Ext.BoxComponent({
				        				  autoEl: {
				        					  tag: 'iframe',
				        					  frameborder: '0',
				        					  src: dirWiki+sparkName+printable
				        				  },
				        				  listeners: {
				        					  afterrender: function () {
				        						  console.log('rendered');

				        						  this.getEl().on('load', function () {
				        							  console.log('loaded');
				        						  });
				        					  }
				        				  }
				        			  });
				        			  new Ext.Window({
				        				  title: 'Spark Help',
				        				  layout:'fit',
				        				  renderTo: Ext.getBody(),
				        				  floating: true,						        	    
				        				  width: 800,
				        				  height: 500,						        	    
				        				  items:[
				        				         iframeContainer
				        				         ]
				        			  }).show();
				        		  }
				        	  }				        	 
				          }
				          ],
				items: [
				        this.grid
				       ]
			});	
		}
		
		this.setPropertyWindowTitle();
		this.identifyCommonProperties();
		this.setCommonPropertiesValues();
		
		// Create the Properties
		
		this.createProperties();
		
		if(this.grid.store.data.length == 0){
			win = new Ext.Window({
				width: 400,		
				height: 210,
				layout: "fit",
				border: false,				
				buttonAlign: 'center',
				buttons: [
							{
							    xtype:'button',						    
							    text:ORYX.I18N.PropertyWindow.helpButton,
							    listeners:{                                        
							        click: function(){							        	
							        	var iframeContainer = new Ext.BoxComponent({
							        	    autoEl: {
							        	        tag: 'iframe',
							        	        frameborder: '0',
							        	        src: dirWiki+sparkName+printable
							        	    },
							        	    listeners: {
							        	        afterrender: function () {
							        	            console.log('rendered');
	
							        	            this.getEl().on('load', function () {
							        	                console.log('loaded');
							        	            });
							        	        }
							        	    }
							        	});
							        	new Ext.Window({
							        	    title: 'Spark Help',
							        	    layout:'fit',
							        	    renderTo: Ext.getBody(),
							        	    floating: true,						        	    
							        	    width: 800,
							        	    height: 500,						        	    
							        	    items:[
							        	           	iframeContainer
							        	           ]
							        	}).show();
							        }
							    }                                        
							}
				          ],
				items: [
				        {
		                     text : ORYX.I18N.PropertyWindow.propertyEmptyText,
		                     style : 'font-size:14px;margin-bottom:10px;display:block;background-color:white;',
		                     anchor : '100%',
		                     xtype : 'label'
		             }
				]
			});
		}
		
		win.show();		
		
	},
	// END Plugin modifications
	
	init: function(){

		// The parent div-node of the grid
		this.node = ORYX.Editor.graft("http://www.w3.org/1999/xhtml",
			null,
			['div']);

		// If the current property in focus is of type 'Date', the date format
		// is stored here.
		this.currentDateFormat;

		// the properties array
		this.popularProperties = [];
		this.properties = [];
		
		/* The currently selected shapes whos properties will shown */
		this.shapeSelection = new Hash();
		this.shapeSelection.shapes = new Array();
		this.shapeSelection.commonProperties = new Array();
		this.shapeSelection.commonPropertiesValues = new Hash();
		
		this.updaterFlag = false;

		// creating the column model of the grid.
		this.columnModel = new Ext.grid.ColumnModel([
			{
				//id: 'name',
				header: ORYX.I18N.PropertyWindow.name,
				dataIndex: 'name',
				width: 90,
				sortable: true,
				renderer: this.tooltipRenderer.bind(this)
			}, {
				//id: 'value',
				header: ORYX.I18N.PropertyWindow.value,
				dataIndex: 'value',
				id: 'propertywindow_column_value',
				width: 110,
				editor: new Ext.form.TextField({
					allowBlank: false
				}),
				renderer: this.renderer.bind(this)
			}/*,
			{
				header: "Pop",
				dataIndex: 'popular',
				hidden: true,
				sortable: true
			}*/
		]);

		/* START - Modificación para evitar el bug al eliminar el GroupStore (Often Used, More properties...)*/
		// create the Data Store
		this.dataSource = new Ext.data.Store({
			proxy: new Ext.data.MemoryProxy(this.properties),
			reader: new Ext.data.ArrayReader({}, [
				{name: 'popular'},
				{name: 'name'},
				{name: 'value'},
				{name: 'icons'},
				{name: 'gridProperties'}
			]),
			sorters: [{
			  property: 'name',
			  direction: 'ASC'
			}]
		});
		this.dataSource.load();	
		

		this.grid = new Ext.grid.EditorGridPanel({
			clicksToEdit: 1,
			stripeRows: true,
			autoExpandColumn: "propertywindow_column_value",
			width:'auto',
			height:'auto',
			hideHeaders: true,
			// the column model
			colModel: this.columnModel,
			enableHdMenu: false,
			view: new Ext.grid.GridView({
				forceFit: true				
			}),
			
			// the data store
			store: this.dataSource
			
		});
		/* END - Modificación para evitar el bug al eliminar el GroupStore (Often Used, More properties...)*/

		/**
		Eliminado del panel de la derecha del editor
		region = this.facade.addToRegion('east', new Ext.Panel({
			width: 220,
			layout: "fit",
			border: false,
			//title: 'Properties',
			items: [
				this.grid 
			]
		}), ORYX.I18N.PropertyWindow.title);
		*/
		

		// Register on Events
		this.grid.on('beforeedit', this.beforeEdit, this, true);
		this.grid.on('afteredit', this.afterEdit, this, true);
		this.grid.view.on('refresh', this.hideMoreAttrs, this, true);
		
		//this.grid.on(ORYX.CONFIG.EVENT_KEYDOWN, this.keyDown, this, true);
		
		// Renderer the Grid
		this.grid.enableColumnMove = false;
		//this.grid.render();

		// Sort as Default the first column
		//this.dataSource.sort('name');

	},
	
	// Select the Canvas when the editor is ready
	selectDiagram: function() {
		this.shapeSelection.shapes = [this.facade.getCanvas()];
		
		this.setPropertyWindowTitle();
		this.identifyCommonProperties();
		this.createProperties();
	},

	specialKeyDown: function(field, event) {
		// If there is a TextArea and the Key is an Enter
		if(field instanceof Ext.form.TextArea && event.button == ORYX.CONFIG.KEY_Code_enter) {
			// Abort the Event
			return false
		}
	},
	tooltipRenderer: function(value, p, record) {
		/* Prepare tooltip */
		p.cellAttr = 'title="' + record.data.gridProperties.tooltip + '"';
		return value;
	},
	
	renderer: function(value, p, record) {
		
		this.tooltipRenderer(value, p, record);
		
		if (record.data.gridProperties.labelProvider) {
		    // there is a label provider to render the value.
		    // we pass it the value
		    return record.data.gridProperties.labelProvider(value);
		}
				
		if(value instanceof Date) {
			// TODO: Date-Schema is not generic
			value = value.dateFormat(ORYX.I18N.PropertyWindow.dateFormat);
		} else if(String(value).search("<a href='") < 0) {
			// Shows the Value in the Grid in each Line
			value = String(value).gsub("<", "&lt;");
			value = String(value).gsub(">", "&gt;");
			value = String(value).gsub("%", "&#37;");
			value = String(value).gsub("&", "&amp;");

			if(record.data.gridProperties.type == ORYX.CONFIG.TYPE_COLOR) {
				value = "<div class='prop-background-color' style='background-color:" + value + "' />";
			}			
			/*
			record.data.icons.each(function(each) {
				if(each.name == value) {
					if(each.icon) {
						value = "<img src='" + each.icon + "' /> " + value;
					}
				}
			});*/
		}

		return value;
	},

	beforeEdit: function(option) {

		var editorGrid 		= this.dataSource.getAt(option.row).data.gridProperties.editor;
		var editorRenderer 	= this.dataSource.getAt(option.row).data.gridProperties.renderer;

		if(editorGrid) {
			// Disable KeyDown
			this.facade.disableEvent(ORYX.CONFIG.EVENT_KEYDOWN);

			option.grid.getColumnModel().setEditor(1, editorGrid);
			
			editorGrid.field.row = option.row;
			// Render the editor to the grid, therefore the editor is also available 
			// for the first and last row
			editorGrid.render(this.grid);
			
			//option.grid.getColumnModel().setRenderer(1, editorRenderer);
			editorGrid.setSize(option.grid.getColumnModel().getColumnWidth(1), editorGrid.height);
		} else {
			return false;
		}
		
		var key = this.dataSource.getAt(option.row).data.gridProperties.propId;
		
		this.oldValues = new Hash();
		this.shapeSelection.shapes.each(function(shape){
			this.oldValues[shape.getId()] = shape.properties[key];
		}.bind(this)); 
	},

	afterEdit: function(option) {
		//Ext1.0: option.grid.getDataSource().commitChanges();
		option.grid.getStore().commitChanges();

		var key 			 = option.record.data.gridProperties.propId;
		var selectedElements = this.shapeSelection.shapes;
		
		var oldValues 	= this.oldValues;	
		
		var newValue	= option.value;
		var facade		= this.facade;
		

		// Implement the specific command for property change
		var commandClass = ORYX.Core.Command.extend({
			construct: function(){
				this.key 		= key;
				this.selectedElements = selectedElements;
				this.oldValues = oldValues;
				this.newValue 	= newValue;
				this.facade		= facade;
			},			
			execute: function(){
				this.selectedElements.each(function(shape){
					if(!shape.getStencil().property(this.key).readonly()) {
						shape.setProperty(this.key, this.newValue);
					}
				}.bind(this));
				this.facade.setSelection(this.selectedElements);
				this.facade.getCanvas().update();
				this.facade.updateSelection();
			},
			rollback: function(){
				this.selectedElements.each(function(shape){
					shape.setProperty(this.key, this.oldValues[shape.getId()]);
				}.bind(this));
				this.facade.setSelection(this.selectedElements);
				this.facade.getCanvas().update();
				this.facade.updateSelection();
			}
		});		
		// Instanciated the class
		var command = new commandClass();
		
		// Execute the command
		this.facade.executeCommands([command]);


		// extended by Kerstin (start)
//
		this.facade.raiseEvent({
			type 		: ORYX.CONFIG.EVENT_PROPWINDOW_PROP_CHANGED, 
			elements	: selectedElements,
			key			: key,
			value		: option.value
		});
		// extended by Kerstin (end)
	},
	
	// Changes made in the property window will be shown directly
	editDirectly:function(key, value){
		
		this.shapeSelection.shapes.each(function(shape){
			if(!shape.getStencil().property(key).readonly()) {
				shape.setProperty(key, value);
				//shape.update();
			}
		}.bind(this));
		
		/* Propagate changed properties */
		var selectedElements = this.shapeSelection.shapes;
		
		this.facade.raiseEvent({
			type 		: ORYX.CONFIG.EVENT_PROPWINDOW_PROP_CHANGED, 
			elements	: selectedElements,
			key			: key,
			value		: value
		});

		this.facade.getCanvas().update();
		
	},
	
	// if a field becomes invalid after editing the shape must be restored to the old value
	updateAfterInvalid : function(key) {
		this.shapeSelection.shapes.each(function(shape) {
			if(!shape.getStencil().property(key).readonly()) {
				shape.setProperty(key, this.oldValues[shape.getId()]);
				shape.update();
			}
		}.bind(this));
		
		this.facade.getCanvas().update();
	},

	// extended by Kerstin (start)	
	dialogClosed: function(data) {
		var row = this.field ? this.field.row : this.row 
		this.scope.afterEdit({
			grid:this.scope.grid, 
			record:this.scope.grid.getStore().getAt(row), 
			//value:this.scope.grid.getStore().getAt(this.row).get("value")
			value: data
		})
		// reopen the text field of the complex list field again
		this.scope.grid.startEditing(row, this.col);
	},
	// extended by Kerstin (end)
	
	/**
	 * Changes the title of the property window panel according to the selected shapes.
	 */
	setPropertyWindowTitle: function() {
		if(this.shapeSelection.shapes.length == 1) {
			// add the name of the stencil of the selected shape to the title
			if(this.shapeSelection.shapes.first().getStencil().title() != ""){
				win.setTitle(ORYX.I18N.PropertyWindow.title +' ('+this.shapeSelection.shapes.first().getStencil().title()+')' );
				// Guardamos en la variable global el nombre del spark para el que se muestra la ventana
				// de propiedades
				sparkName = this.shapeSelection.shapes.first().getStencil().title();
			}else
				win.setTitle(ORYX.I18N.PropertyWindow.title);
		} else {
			win.setTitle(ORYX.I18N.PropertyWindow.title +' ('
							+ this.shapeSelection.shapes.length
							+ ' '
							+ ORYX.I18N.PropertyWindow.selected 
							+')');
		}
	},
	/**
	 * Sets this.shapeSelection.commonPropertiesValues.
	 * If the value for a common property is not equal for each shape the value
	 * is left empty in the property window.
	 */
	setCommonPropertiesValues: function() {
		this.shapeSelection.commonPropertiesValues = new Hash();
		this.shapeSelection.commonProperties.each(function(property){
			var key = property.prefix() + "-" + property.id();
			var emptyValue = false;
			var firstShape = this.shapeSelection.shapes.first();
			
			this.shapeSelection.shapes.each(function(shape){
				if(firstShape.properties[key] != shape.properties[key]) {
					emptyValue = true;
				}
			}.bind(this));
			
			/* Set property value */
			if(!emptyValue) {
				this.shapeSelection.commonPropertiesValues[key]
					= firstShape.properties[key];
			}
		}.bind(this));
	},
	
	/**
	 * Returns the set of stencils used by the passed shapes.
	 */
	getStencilSetOfSelection: function() {
		var stencils = new Hash();
		
		this.shapeSelection.shapes.each(function(shape) {
			stencils[shape.getStencil().id()] = shape.getStencil();
		})
		return stencils;
	},
	
	/**
	 * Identifies the common Properties of the selected shapes.
	 */
	identifyCommonProperties: function() {
		this.shapeSelection.commonProperties.clear();
		
		/* 
		 * A common property is a property, that is part of 
		 * the stencil definition of the first and all other stencils.
		 */
		var stencils = this.getStencilSetOfSelection();
		var firstStencil = stencils.values().first();
		var comparingStencils = stencils.values().without(firstStencil);
		
		
		if(comparingStencils.length == 0) {
			this.shapeSelection.commonProperties = firstStencil.properties();
		} else {
			var properties = new Hash();
			
			/* put all properties of on stencil in a Hash */
			firstStencil.properties().each(function(property){
				properties[property.namespace() + '-' + property.id() 
							+ '-' + property.type()] = property;
			});
			
			/* Calculate intersection of properties. */
			
			comparingStencils.each(function(stencil){
				var intersection = new Hash();
				stencil.properties().each(function(property){
					if(properties[property.namespace() + '-' + property.id()
									+ '-' + property.type()]){
						intersection[property.namespace() + '-' + property.id()
										+ '-' + property.type()] = property;
					}
				});
				properties = intersection;	
			});
			
			this.shapeSelection.commonProperties = properties.values();
		}
	},
	
	onSelectionChanged: function(event) {
		// Plugin modifications to show properties in a new window (not in the right panel)
		// if there is an open property window
		// when the current selection change,
		// we close that property window
		if(win)
			win.close();
		/* Event to call afterEdit method */
		this.grid.stopEditing();
		
		/* Selected shapes */
		this.shapeSelection.shapes = event.elements;
		
		/* Case: nothing selected */
		if(event.elements.length == 0) {
			this.shapeSelection.shapes = [this.facade.getCanvas()];
		}
		
		/* subselection available */
		if(event.subSelection){
			this.shapeSelection.shapes = [event.subSelection];
		}
		
		this.setPropertyWindowTitle();
		this.identifyCommonProperties();
		this.setCommonPropertiesValues();
		
		// Create the Properties
		
		this.createProperties();		
		
	},
	
	/**
	 * Creates the properties for the ExtJS-Grid from the properties of the
	 * selected shapes.
	 */
	createProperties: function() {
		this.properties = [];
		this.popularProperties = [];
		
		if(this.shapeSelection.commonProperties) {				
			// add new property lines
			this.shapeSelection.commonProperties.each((function(pair, index) {
				if(pair.id()!="name" && pair.id()!="bgColor" && pair.id()!='bgcolor'){
					var key = pair.prefix() + "-" + pair.id();
					
					// Get the property pair
					var name		= pair.title();
					var icons		= [];
					var attribute	= this.shapeSelection.commonPropertiesValues[key];
					
					var editorGrid = undefined;
					var editorRenderer = null;
					
					var refToViewFlag = false;
	
					var editorClass = ORYX.FieldEditors[pair.type()];
					 
					if (editorClass !== undefined) {
						editorGrid = editorClass.init.bind(this, key, pair, icons, index)();
						if (editorGrid == null) {
							return; // don't insist, the editor won't be created this time around.
						}
						// Register Event to enable KeyDown
						editorGrid.on('beforehide', this.facade.enableEvent.bind(this, ORYX.CONFIG.EVENT_KEYDOWN));
						editorGrid.on('specialkey', this.specialKeyDown.bind(this));
					} else {
						//if(pair.id()!='name' && pair.id()!='bgColor'){}
						if(!pair.readonly()){
							switch(pair.type()) {
							case ORYX.CONFIG.TYPE_STRING:
								// If the Text is MultiLine
								if(pair.wrapLines()) {
									// Set the Editor as TextArea
									var editorTextArea = new Ext.form.TextArea({alignment: "tl-tl", allowBlank: pair.optional(),  msgTarget:'title', maxLength:pair.length()});
									editorTextArea.on('keyup', function(textArea, event) {
										this.editDirectly(key, textArea.getValue());
									}.bind(this));								
	
									editorGrid = new Ext.Editor(editorTextArea);
								} else {
									// If not, set the Editor as InputField
									var editorInput = new Ext.form.TextField({allowBlank: pair.optional(),  msgTarget:'title', maxLength:pair.length()});
									editorInput.on('keyup', function(input, event) {
										this.editDirectly(key, input.getValue());
									}.bind(this));
	
									// reverts the shape if the editor field is invalid
									editorInput.on('blur', function(input) {
										if(!input.isValid(false))
											this.updateAfterInvalid(key);
									}.bind(this));
	
									editorInput.on("specialkey", function(input, e) {
										if(!input.isValid(false))
											this.updateAfterInvalid(key);
									}.bind(this));
	
									editorGrid = new Ext.Editor(editorInput);
								}
								break;
							case ORYX.CONFIG.TYPE_BOOLEAN:
								// Set the Editor as a CheckBox
								var editorCheckbox = new Ext.form.Checkbox();
								editorCheckbox.on('check', function(c,checked) {
									this.editDirectly(key, checked);
								}.bind(this));
	
								editorGrid = new Ext.Editor(editorCheckbox);
								break;
							case ORYX.CONFIG.TYPE_INTEGER:
								// Set as an Editor for Integers
								var numberField = new Ext.form.NumberField({allowBlank: pair.optional(), allowDecimals:false, msgTarget:'title', minValue: pair.min(), maxValue: pair.max()});
								numberField.on('keyup', function(input, event) {
									this.editDirectly(key, input.getValue());
								}.bind(this));							
	
								editorGrid = new Ext.Editor(numberField);
								break;
							case ORYX.CONFIG.TYPE_FLOAT:
								// Set as an Editor for Float
								var numberField = new Ext.form.NumberField({ allowBlank: pair.optional(), allowDecimals:true, msgTarget:'title', minValue: pair.min(), maxValue: pair.max()});
								numberField.on('keyup', function(input, event) {
									this.editDirectly(key, input.getValue());
								}.bind(this));
	
								editorGrid = new Ext.Editor(numberField);
	
								break;
							case ORYX.CONFIG.TYPE_COLOR:
								// Set as a ColorPicker
								// Ext1.0 editorGrid = new gEdit(new form.ColorField({ allowBlank: pair.optional(),  msgTarget:'title' }));
	
								var editorPicker = new Ext.ux.ColorField({ allowBlank: pair.optional(),  msgTarget:'title', facade: this.facade });
	
								/*this.facade.registerOnEvent(ORYX.CONFIG.EVENT_COLOR_CHANGE, function(option) {
									this.editDirectly(key, option.value);
								}.bind(this));*/
	
								editorGrid = new Ext.Editor(editorPicker);
	
								break;
							case ORYX.CONFIG.TYPE_CHOICE:
								var items = pair.items();
	
								var options = [];
								items.each(function(value) {
									if(value.value() == attribute)
										attribute = value.title();
	
									if(value.refToView()[0] && value.refToView()[0]!=' ' && value.refToView()[0]!='')
										refToViewFlag = true;
	
									options.push([value.icon(), value.title(), value.value()]);
	
									icons.push({
										name: value.title(),
										icon: value.icon()
									});
								});
	
								var store = new Ext.data.SimpleStore({
									fields: [{name: 'icon'},
									         {name: 'title'},
									         {name: 'value'}	],
									         data : options // from states.js
								});
	
								// Set the grid Editor
	
								var editorCombo = new Ext.form.ComboBox({
									tpl: '<tpl for="."><div class="x-combo-list-item">{[(values.icon) ? "<img src=\'" + values.icon + "\' />" : ""]} {title}</div></tpl>',
									store: store,
									displayField:'title',
									valueField: 'value',
									typeAhead: true,
									mode: 'local',
									triggerAction: 'all',
									selectOnFocus:true
								});
	
								editorCombo.on('select', function(combo, record, index) {
									this.editDirectly(key, combo.getValue());
								}.bind(this))
	
								editorGrid = new Ext.Editor(editorCombo);
	
								break;
							case ORYX.CONFIG.TYPE_DATE:
								var currFormat = ORYX.I18N.PropertyWindow.dateFormat
								if(!(attribute instanceof Date))
									attribute = Date.parseDate(attribute, currFormat)
									editorGrid = new Ext.Editor(new Ext.form.DateField({ allowBlank: pair.optional(), format:currFormat,  msgTarget:'title'}));
								break;
								
								
								////////////////////////////////
								//    RAUL EXTENSION START    //
								////////////////////////////////
							case ORYX.CONFIG.TYPE_PROMPT:
								
								
								var cf = new Ext.form.PromptField({
									allowBlank: pair.optional(),
									dataSource:this.dataSource,
									grid:this.grid,
									row:index,
									facade:this.facade
								});
								cf.on('dialogClosed', this.dialogClosed, {scope:this, row:index, col:1,field:cf});							
								editorGrid = new Ext.Editor(cf);
								break;
								
								break;
								
								////////////////////////////////
								//    RAUL EXTENSION END    //
								////////////////////////////////
								
							case ORYX.CONFIG.TYPE_COMPONENT:
								
								
								var cf = new Ext.form.ComponentField({
									allowBlank: pair.optional(),
									dataSource:this.dataSource,
									grid:this.grid,
									row:index,
									facade:this.facade
								});
								cf.on('dialogClosed', this.dialogClosed, {scope:this, row:index, col:1,field:cf});							
								editorGrid = new Ext.Editor(cf);
								break;
								
								break;
	
							case ORYX.CONFIG.TYPE_TEXT:
	
								var cf = new Ext.form.ComplexTextField({
									allowBlank: pair.optional(),
									dataSource:this.dataSource,
									grid:this.grid,
									row:index,
									facade:this.facade
								});
								cf.on('dialogClosed', this.dialogClosed, {scope:this, row:index, col:1,field:cf});							
								editorGrid = new Ext.Editor(cf);
								break;
	
								// extended by Kerstin (start)
							case ORYX.CONFIG.TYPE_COMPLEX:
	
								var cf = new Ext.form.ComplexListField({ allowBlank: pair.optional()}, pair.complexItems(), key, this.facade);
								cf.on('dialogClosed', this.dialogClosed, {scope:this, row:index, col:1,field:cf});							
								editorGrid = new Ext.Editor(cf);
								break;
								// extended by Kerstin (end)
								
							// extended by Ana Paula (start)
							case ORYX.CONFIG.TYPE_FILE:
								// Abrir un nuevo diálogo que permita acceder al sistema de ficheos para seleccionar un
								// archivo
								
	//							var editorInput = new Ext.form.TextField(
	//									{
	//										id: 'fileTextField',
	//										allowBlank: pair.optional(),										
	//										maxLength:pair.length(), 
	//										enableKeyEvents: true,
	//										value: "Seleccione un archivo"
	//									});
	//							
	//							
	//						
	//							editorInput.on('focus', function(input, event) {
	//								this.showUploadDialog(editorInput);		
	//								/*this.editDirectly(key, input.getValue());*/
	//							}.bind(this));
								
								var editorInput = new Ext.form.TextField(
										{
											id: 'fileTextField',
											allowBlank: pair.optional(),										
											maxLength:pair.length(), 
											enableKeyEvents: true,
											value: "Seleccione un archivo"
										});
								
								
								editorInput.on('dialogClosed', this.dialogClosed, {scope:this, row:index, col:1,field:editorInput});
								editorInput.on('focus', function(input, event) {
									this.showFileEditorDialog(editorInput);								
								}.bind(this));
								editorGrid = new Ext.Editor(editorInput);
								break;
							// extended by Ana Paula (end)
								
							// extended by Ana Paula (start)
							case ORYX.CONFIG.TYPE_FILE_BROWSER:							
								var editorInput = new Ext.form.TextField(
										{
											id: 'fileBroserField',
											allowBlank: pair.optional(),										
											maxLength:pair.length(), 
											enableKeyEvents: true,
											value: "Seleccione un archivo"
										});
								
								
							
								editorInput.on('focus', function(input, event) {
									this.showFileBrowserDialog(editorInput);								
								}.bind(this));
								editorGrid = new Ext.Editor(editorInput);
								break;
							// extended by Ana Paula (end)
	
							// extended by Gerardo (Start)
							case "CPNString":
								var editorInput = new Ext.form.TextField(
										{
											allowBlank: pair.optional(),
											msgTarget:'title', 
											maxLength:pair.length(), 
											enableKeyEvents: true
										});
	
								editorInput.on('keyup', function(input, event) {
									this.editDirectly(key, input.getValue());
								}.bind(this));
	
								editorGrid = new Ext.Editor(editorInput);							
								break;
								// extended by Gerardo (End)
	
							default:
								var editorInput = new Ext.form.TextField({ allowBlank: pair.optional(),  msgTarget:'title', maxLength:pair.length(), enableKeyEvents: true});
							editorInput.on('keyup', function(input, event) {
								this.editDirectly(key, input.getValue());
							}.bind(this));
	
							editorGrid = new Ext.Editor(editorInput);
							}
	
	
							// Register Event to enable KeyDown
							editorGrid.on('beforehide', this.facade.enableEvent.bind(this, ORYX.CONFIG.EVENT_KEYDOWN));
							editorGrid.on('specialkey', this.specialKeyDown.bind(this));
	
						} else if(pair.type() === ORYX.CONFIG.TYPE_URL || pair.type() === ORYX.CONFIG.TYPE_DIAGRAM_LINK){
							attribute = String(attribute).search("http") !== 0 ? ("http://" + attribute) : attribute;
							attribute = "<a href='" + attribute + "' target='_blank'>" + attribute.split("://")[1] + "</a>"
						}
					}
					
					// Push to the properties-array
					if(pair.visible()) {
						// Popular Properties are those with a refToView set or those which are set to be popular
						if ((pair.refToView()[0] && pair.refToView()[0]!='' && pair.refToView()[0]!=' ') || refToViewFlag || pair.popular()) {
							pair.setPopular();
						} 
						
						
						if(pair.popular()) {
							this.popularProperties.push([pair.popular(), name, attribute, icons, {
								editor: editorGrid,
								propId: key,
								type: pair.type(),
								tooltip: pair.description(),
								renderer: editorRenderer,
								labelProvider: this.getLabelProvider(pair)
							}]);
						}
						// Evitar que se muestren las propiedades que no están marcadas
						// como populares
	//					else {					
	//						this.properties.push([pair.popular(), name, attribute, icons, {
	//							editor: editorGrid,
	//							propId: key,
	//							type: pair.type(),
	//							tooltip: pair.description(),
	//							renderer: editorRenderer,
	//							labelProvider: this.getLabelProvider(pair)
	//						}]);
	//					}
					}
				}
			}).bind(this));
		}

		this.setProperties();
	},
	
	showFileBrowserDialog: function(editor){
		var winFB = new Ext.Window({
			  title: 'FileBrowser',
			  layout: 'fit',
			  width: 500,
			  height: 400,
			  items: [{
			    xtype: 'filebrowserpanel'
			  }]
			});

			winFB.show();
	},
	
	showFileEditorDialog: function(editor){	
		
		var url = location.protocol + '//' + location.host + ORYX.CONFIG.FILE_PROPERTY_UPLOAD_URL +"?user="+ORYX.USERNAME+"&sparkName=" + sparkName + "&fileName=";
		
		var form = new Ext.form.FormPanel({
	             baseCls : 'x-plain',	             
	             defaultType : 'textfield',  
	             fileUpload: true,	             
	             items: [                     
	                     {
	                    	 xtype:'fieldset',
	                    	 checkboxToggle:true,
	                    	 title: ORYX.I18N.FILE_PROPERTY.loadFiles,
	                    	 autoHeight:true,	                    	 
	                    	 defaultType: 'textfield',
	                    	 collapsed: true,
	                    	 items :[            	         
	                    	         {
	                    	        	 fieldLabel : ORYX.I18N.FILE_PROPERTY.localFileText,
	                    	        	 name 		: 'subject',
	                    	        	 inputType 	: 'file',                            
	                    	        	 style 		: 'margin-bottom:18px;display:block;',
	                    	        	 itemCls 	: 'ext_specific_window_overflow',
	                    	        	 id			: 'uploadFileField'
	                    	         },	            	         
	                    	         {	            	        	 
	                    	        	 xtype	: 'label',
	                    	        	 text	: ORYX.I18N.FILE_PROPERTY.serverFileText + ':',	                    	        	 
	                    	         },
	                    	         {
	                    	        	 text	: ORYX.I18N.FILE_PROPERTY.serverFile, 	        	 
	                    	        	 xtype	: 'button',
	                    	        	 id		: 'serverFileButton',	            	        	 
	                    	        	 iconCls: 'server-search-icon',	
	                    	        	 handler: function(){
	                    	        		 var winFB = new Ext.Window({
	                    	        			 title: ORYX.I18N.FILE_PROPERTY.selectServerFile,
	                    	        			 layout: 'fit',
	                    	        			 width: 500,
	                    	        			 height: 200,
	                    	        			 items: [{
	                    	        				 xtype	: 'selectfilebrowser',
	                    	        				 id		: 'serverFileBrowser'
	                    	        			 }]
	                    	        		 });

	                    	        		 this.selectfilebrowser = winFB.items.itemAt(0);	            	        		 
	                    	        		 this.selectfilebrowser.on('clickfile', function(input, record) {
	                    	        			 var filename = record.data.name; 
	                    	        			 // Cargar el valor de nombre de archivo
	                    	        			 form.items.items[1].setValue(filename);	
	                    	        			 var url = location.protocol + '//' + location.host + ORYX.CONFIG.FILE_PROPERTY_DOWNLOAD_FILE_URL 
	                    	        			 + "?user="+ORYX.USERNAME+"&sparkName=" + sparkName + "&fileName=" + filename;
	                    	        			 // y su contenido
	                    	        			 Ext.Ajax.request({
	                    	        				 url 		: url,     	                    				                     				
	                    	        				 success	: function(response) {	            	        					 				
	                    	        					 this.editorCodemirror.setValue(response.responseText);
	                    	        				 }.bind(this),
	                    	        				 failure 	: function() {	                    				
	                    	        					 Ext.Msg.alert(ORYX.I18N.FILE_PROPERTY.downloadError);
	                    	        				 }                                                    
	                    	        			 });
	                    	        		 }.bind(this));
	                    	        		 winFB.show();
	                    	        	 }.bind(this)
	                    	         }]
	                     },             
	                     {
	                    	 xtype		: 'textfield',        
	                    	 name		: 'filename',
	                    	 fieldLabel	: ORYX.I18N.FILE_PROPERTY.filename
	                     },
	                     {
	                    	 xtype 		: 'textarea',
	                    	 hideLabel 	: true,            	 
	                    	 name 		: 'content',
	                    	 anchor 	: '100% -63',
	                    	 id 		: 'editorArea'
	                     }]             
		});     
	     
	     // Create the panel
		var fileDialog = new Ext.Window({			
				autoCreate 	: true,
				layout 		: 'fit',
				plain 		: true,
				bodyStyle 	: 'padding:5px;',
				title 		: ORYX.I18N.FILE_PROPERTY.uploadTitle,
				height		: 525,
				width 		: 750,
				modal 		: true,
				fixedcenter : true,
				shadow 		: true,
				proxyDrag 	: true,
				resizable 	: true,
				items 		: [ form ],
				listeners	: {
						hide: function(){							 					
							fileDialog.destroy(true);
							delete fileDialog;
						}.bind(this)				
				},	 			
				buttons 	: [
				        	   {
				        		   text		: ORYX.I18N.FILE_PROPERTY.generateTemplate,	
				        		   iconCls	: 'template-file-icon',
				        		   handler	: function(){          		 
				        			   var template = sparkName.indexOf("Script") != -1 ? scriptTemplate : aimlTemplate;
				        			   var url = location.protocol + '//' + location.host + loadTemplateServlet + "?template=" + template;

				        			   form.items.items[1].setValue(template);

				        			   Ext.Ajax.request({
				        				   url : url,     	                    				                     				
				        				   success : function(response) {	            	        					 				
				        					   this.editorCodemirror.setValue(response.responseText);
				        				   }.bind(this),
				        				   failure : function() {	                    				
				        					   Ext.Msg.alert(ORYX.I18N.FILE_PROPERTY.downloadError);
				        				   }                                                    
				        			   });            		 
				        		   }.bind(this)	            	 

				        	   },                   
				        	   {
				        		   text 	: ORYX.I18N.Save.save,                     
				        		   handler 	: function() {                    	 
				        			   // Compruebo si hay algún archivo para subir
				        			   var filename = form.items.itemAt(1).getValue();	                    	 
				        			   // y que el contenido no sea vacío
				        			   var editorContent = this.editorCodemirror.getValue();

				        			   if(filename != '' && editorContent != ''){	                    		 
				        				   // Guardar el valor del editor en el textarea para enviarlo como parámetro
				        				   this.editorCodemirror.save();
				        				   // Enviar el formulario
				        				   form.getForm().submit({
				        					   url		: url + filename,
				        					   scope	:this,
				        					   success 	: function(response) {	            	        					 				
				        						   Ext.Msg.alert(ORYX.I18N.FILE_PROPERTY.uploadSuccess);
				        					   },        		 		
				        					   waitMsg	:ORYX.I18N.FILE_PROPERTY.savingData
				        				   });
				        			   }
				        			   else {
				        				   // Avisar al usuario de que debe seleccionar un fichero
				        				   var msg = filename ? ORYX.I18N.FILE_PROPERTY.mandatoryFileContent : ORYX.I18N.FILE_PROPERTY.mandatoryFile
				        						   Ext.Msg.alert(msg);
				        			   }
				        		   }.bind(this)
				        	   }, 
				        	   {
				        		   text 	: ORYX.I18N.FILE_PROPERTY.closeButtonText,
				        		   handler 	: function() {
				        			   fileDialog.hide();
				        		   }.bind(this)
				        	   } 
				        	   ]
		});       

		// Show the panel    	    
		fileDialog.show();  
		// Para ocultar el textfield y que sólo se vea el fileDialog
		this.grid.stopEditing();     

		// Alinear el botón abrir fichero de servidor con su etiqueta en la misma línea
		var serverFileButton = Ext.get("serverFileButton");	     
		serverFileButton.alignTo(Ext.get("uploadFileField"));    
		serverFileButton.setStyle("margin-top","16px");
		// Añadir estilos, no funciona añadir una clase
		serverFileButton.child('button').setStyle("font-size","12px");
		serverFileButton.child('button').setStyle("height","20px");
		serverFileButton.child('button').setStyle("padding-left","24px");

		// Estilos para el botón 'Generar plantilla'
		fileDialog.buttons[0].getEl().child('button').setStyle("font-size","12px");	     
		fileDialog.buttons[0].getEl().position("absolute");
		fileDialog.buttons[0].getEl().applyStyles({left:"14px",bottom:"14px"});
		
		// Cargar editor para Javascript y AIML, en el lugar donde se encontraba el 'textarea'
		this.editorCodemirror = CodeMirror.fromTextArea(document.getElementById("editorArea"), sparkName.indexOf("Script") != -1 ? 
	    		 {
	    	 		 lineNumbers				: true,
	    	 		 styleActiveLine			: true,
	    	 		 highlightSelectionMatches	: true,
	    	 		 autoCloseBrackets			: true,
	    	 		 gutters					: ["CodeMirror-lint-markers"],
	    	 	 	 lintWith					: CodeMirror.javascriptValidator,
	    	 		 extraKeys					: {"Ctrl-Space": "autocomplete"}
	    		 }:
	    		 {
	    			 mode						: 'xml',	    			 
	    			 lineNumbers				: true,
	    			 styleActiveLine			: true,
	    			 highlightSelectionMatches	: true ,
	    			 extraKeys: {
	    				 "'<'": completeAfter,
	    				 "'/'": completeIfAfterLt,
	    				 "' '": completeIfInTag,
	    				 "'='": completeIfInTag,
	    				 "Ctrl-Space": function(cm) {
	    					 CodeMirror.showHint(cm, CodeMirror.xmlHint, {schemaInfo: tags});
	    				 }
	    			 }
	    		 }
		);
	    // Función autocompletar para editor modo 'Javascript'
		CodeMirror.commands.autocomplete = function(cm) {
	    	 CodeMirror.showHint(cm, CodeMirror.javascriptHint);
	     
		};		
		// Adds the change event handler to	     
		form.items.items[0].items.items[0].getEl().dom.addEventListener('change',
	                     function(evt) {
	                             var reader = new FileReader();  
	                             var file = evt.target.files[0];
	                             
	                             form.items.items[1].setValue(evt.target.files[0].name);	                             
	                             //Bugfix for Chrome	                             
	                             if(typeof(FileReader.prototype.addEventListener) === "function") {
	                                     reader.addEventListener("loadend", function(evt) {                                    
	                                             this.editorCodemirror.setValue(evt.target.result);
	                                     }.bind(this), false);
	                                    
	                             } else {
	                                     reader.onload = function(evt) {                                        
	                                             form.items.items[2].setValue(evt.target.result);
	                                     };                                              
	                             }
	                             reader.readAsText(file,"UTF-8");
	                     }.bind(this), true);  
	},
	
	/**
	 * Gets a label provider from the registered label providers
	 * according to the id of the label provider registered on the stencil.
	 */
    getLabelProvider: function(stencil) {
       lp = ORYX.LabelProviders[stencil.labelProvider()];
       if (lp) {
           return lp(stencil);
       }
       return null;
    },
	
	hideMoreAttrs: function(panel) {
		// TODO: Implement the case that the canvas has no attributes
		if (this.properties.length <= 0){ return }
		
		// collapse the "more attr" group
		this.grid.view.toggleGroup(this.grid.view.getGroupId(this.properties[0][0]), false);
		
		// prevent the more attributes pane from closing after a attribute has been edited
		this.grid.view.un("refresh", this.hideMoreAttrs, this);
	},

	setProperties: function() {
		var props = this.popularProperties.concat(this.properties);
		
		this.dataSource.loadData(props);
	}
};
ORYX.Plugins.PropertyWindow = Clazz.extend(ORYX.Plugins.PropertyWindow);



/**
 * Editor for complex type
 * 
 * When starting to edit the editor, it creates a new dialog where new attributes
 * can be specified which generates json out of this and put this 
 * back to the input field.
 * 
 * This is implemented from Kerstin Pfitzner
 * 
 * @param {Object} config
 * @param {Object} items
 * @param {Object} key
 * @param {Object} facade
 */


Ext.form.ComplexListField = function(config, items, key, facade){
    Ext.form.ComplexListField.superclass.constructor.call(this, config);
	this.items 	= items;
	this.key 	= key;
	this.facade = facade;
};

/**
 * This is a special trigger field used for complex properties.
 * The trigger field opens a dialog that shows a list of properties.
 * The entered values will be stored as trigger field value in the JSON format.
 */
Ext.extend(Ext.form.ComplexListField, Ext.form.TriggerField,  {
	/**
     * @cfg {String} triggerClass
     * An additional CSS class used to style the trigger button.  The trigger will always get the
     * class 'x-form-trigger' and triggerClass will be <b>appended</b> if specified.
     */
    triggerClass:	'x-form-complex-trigger',
	readOnly:		true,
	emptyText: 		ORYX.I18N.PropertyWindow.clickIcon,
		
	/**
	 * Builds the JSON value from the data source of the grid in the dialog.
	 */
	buildValue: function() {
		var ds = this.grid.getStore();
		ds.commitChanges();
		
		if (ds.getCount() == 0) {
			return "";
		}
		
		var jsonString = "[";
		for (var i = 0; i < ds.getCount(); i++) {
			var data = ds.getAt(i);		
			jsonString += "{";	
			for (var j = 0; j < this.items.length; j++) {
				var key = this.items[j].id();
				jsonString += key + ':' + ("" + data.get(key)).toJSON();
				if (j < (this.items.length - 1)) {
					jsonString += ", ";
				}
			}
			jsonString += "}";
			if (i < (ds.getCount() - 1)) {
				jsonString += ", ";
			}
		}
		jsonString += "]";
		
		jsonString = "{'totalCount':" + ds.getCount().toJSON() + 
			", 'items':" + jsonString + "}";
		return Object.toJSON(jsonString.evalJSON());
	},
	
	/**
	 * Returns the field key.
	 */
	getFieldKey: function() {
		return this.key;
	},
	
	/**
	 * Returns the actual value of the trigger field.
	 * If the table does not contain any values the empty
	 * string will be returned.
	 */
    getValue : function(){
		// return actual value if grid is active
		if (this.grid) {
			return this.buildValue();			
		} else if (this.data == undefined) {
			return "";
		} else {
			return this.data;
		}
    },
	
	/**
	 * Sets the value of the trigger field.
	 * In this case this sets the data that will be shown in
	 * the grid of the dialog.
	 * 
	 * @param {Object} value The value to be set (JSON format or empty string)
	 */
	setValue: function(value) {	
		if (value.length > 0) {
			// set only if this.data not set yet
			// only to initialize the grid
			if (this.data == undefined) {
				this.data = value;
			}
		}
	},
	
	/**
	 * Returns false. In this way key events will not be propagated
	 * to other elements.
	 * 
	 * @param {Object} event The keydown event.
	 */
	keydownHandler: function(event) {
		return false;
	},
	
	/**
	 * The listeners of the dialog. 
	 * 
	 * If the dialog is hidded, a dialogClosed event will be fired.
	 * This has to be used by the parent element of the trigger field
	 * to reenable the trigger field (focus gets lost when entering values
	 * in the dialog).
	 */
    dialogListeners : {
        show : function(){ // retain focus styling
            this.onFocus();	
			this.facade.registerOnEvent(ORYX.CONFIG.EVENT_KEYDOWN, this.keydownHandler.bind(this));
			this.facade.disableEvent(ORYX.CONFIG.EVENT_KEYDOWN);
			return;
        },
        hide : function(){

            var dl = this.dialogListeners;
            this.dialog.un("show", dl.show,  this);
            this.dialog.un("hide", dl.hide,  this);
			
			this.dialog.destroy(true);
			this.grid.destroy(true);
			delete this.grid;
			delete this.dialog;
			
			this.facade.unregisterOnEvent(ORYX.CONFIG.EVENT_KEYDOWN, this.keydownHandler.bind(this));
			this.facade.enableEvent(ORYX.CONFIG.EVENT_KEYDOWN);
			
			// store data and notify parent about the closed dialog
			// parent has to handel this event and start editing the text field again
			this.fireEvent('dialogClosed', this.data);
			
			Ext.form.ComplexListField.superclass.setValue.call(this, this.data);
        }
    },	
	
	/**
	 * Builds up the initial values of the grid.
	 * 
	 * @param {Object} recordType The record type of the grid.
	 * @param {Object} items      The initial items of the grid (columns)
	 */
	buildInitial: function(recordType, items) {
		var initial = new Hash();
		
		for (var i = 0; i < items.length; i++) {
			var id = items[i].id();
			initial[id] = items[i].value();
		}
		
		var RecordTemplate = Ext.data.Record.create(recordType);
		return new RecordTemplate(initial);
	},
	
	/**
	 * Builds up the column model of the grid. The parent element of the
	 * grid.
	 * 
	 * Sets up the editors for the grid columns depending on the 
	 * type of the items.
	 * 
	 * @param {Object} parent The 
	 */
	buildColumnModel: function(parent) {
		var cols = [];
		for (var i = 0; i < this.items.length; i++) {
			var id 		= this.items[i].id();
			var header 	= this.items[i].name();
			var width 	= this.items[i].width();
			var type 	= this.items[i].type();
			var editor;
			
			if (type == ORYX.CONFIG.TYPE_STRING) {
				editor = new Ext.form.TextField({ allowBlank : this.items[i].optional(), width : width});
			} else if (type == ORYX.CONFIG.TYPE_CHOICE) {				
				var items = this.items[i].items();
				var select = ORYX.Editor.graft("http://www.w3.org/1999/xhtml", parent, ['select', {style:'display:none'}]);
				var optionTmpl = new Ext.Template('<option value="{value}">{value}</option>');
				items.each(function(value){ 
					optionTmpl.append(select, {value:value.value()}); 
				});				
				
				editor = new Ext.form.ComboBox(
					{ typeAhead: true, triggerAction: 'all', transform:select, lazyRender:true,  msgTarget:'title', width : width});			
			} else if (type == ORYX.CONFIG.TYPE_BOOLEAN) {
				editor = new Ext.form.Checkbox( { width : width } );
			} else if (type == "xpath") {
				//TODO set the xpath type as string, same editor as string.
				editor = new Ext.form.TextField({ allowBlank : this.items[i].optional(), width : width});
			}
					
			cols.push({
				id: 		id,
				header: 	header,
				dataIndex: 	id,
				resizable: 	true,
				editor: 	editor,
				width:		width
	        });
			
		}
		return new Ext.grid.ColumnModel(cols);
	},
	
	/**
	 * After a cell was edited the changes will be commited.
	 * 
	 * @param {Object} option The option that was edited.
	 */
	afterEdit: function(option) {
		option.grid.getStore().commitChanges();
	},
		
	/**
	 * Before a cell is edited it has to be checked if this 
	 * cell is disabled by another cell value. If so, the cell editor will
	 * be disabled.
	 * 
	 * @param {Object} option The option to be edited.
	 */
	beforeEdit: function(option) {

		var state = this.grid.getView().getScrollState();
		
		var col = option.column;
		var row = option.row;
		var editId = this.grid.getColumnModel().config[col].id;
		// check if there is an item in the row, that disables this cell
		for (var i = 0; i < this.items.length; i++) {
			// check each item that defines a "disable" property
			var item = this.items[i];
			var disables = item.disable();
			if (disables != undefined) {
				
				// check if the value of the column of this item in this row is equal to a disabling value
				var value = this.grid.getStore().getAt(row).get(item.id());
				for (var j = 0; j < disables.length; j++) {
					var disable = disables[j];
					if (disable.value == value) {
						
						for (var k = 0; k < disable.items.length; k++) {
							// check if this value disables the cell to select 
							// (id is equals to the id of the column to edit)
							var disItem = disable.items[k];
							if (disItem == editId) {
								this.grid.getColumnModel().getCellEditor(col, row).disable();
								return;
							}
						}
					}
				}		
			}
		}
		this.grid.getColumnModel().getCellEditor(col, row).enable();
		//this.grid.getView().restoreScroll(state);
	},
	
    /**
     * If the trigger was clicked a dialog has to be opened
     * to enter the values for the complex property.
     */
    onTriggerClick : function(){
        if(this.disabled){
            return;
        }	
		
		//if(!this.dialog) { 
		
			var dialogWidth = 0;
			var recordType 	= [];
			
			for (var i = 0; i < this.items.length; i++) {
				var id 		= this.items[i].id();
				var width 	= this.items[i].width();
				var type 	= this.items[i].type();	
					
				if (type == ORYX.CONFIG.TYPE_CHOICE) {
					type = ORYX.CONFIG.TYPE_STRING;
				}
						
				dialogWidth += width;
				recordType[i] = {name:id, type:type};
			}			
			
			if (dialogWidth > 800) {
				dialogWidth = 800;
			}
			dialogWidth += 22;
			
			var data = this.data;
			if (data == "") {
				// empty string can not be parsed
				data = "{}";
			}
			
			
			var ds = new Ext.data.Store({
		        proxy: new Ext.data.MemoryProxy(eval("(" + data + ")")),				
				reader: new Ext.data.JsonReader({
		            root: 'items',
		            totalProperty: 'totalCount'
		        	}, recordType)
	        });
			ds.load();
					
				
			var cm = this.buildColumnModel();
			
			this.grid = new Ext.grid.EditorGridPanel({
				store:		ds,
		        cm:			cm,
				stripeRows: true,
				clicksToEdit : 1,
				autoHeight:true,
		        selModel: 	new Ext.grid.CellSelectionModel()
		    });	
			
									
			//var gridHead = this.grid.getView().getHeaderPanel(true);
			var toolbar = new Ext.Toolbar(
			[{
				text: ORYX.I18N.PropertyWindow.add,
				handler: function(){
					var ds = this.grid.getStore();
					var index = ds.getCount();
					this.grid.stopEditing();
					var p = this.buildInitial(recordType, this.items);
					ds.insert(index, p);
					ds.commitChanges();
					this.grid.startEditing(index, 0);
				}.bind(this)
			},{
				text: ORYX.I18N.PropertyWindow.rem,
		        handler : function(){
					var ds = this.grid.getStore();
					var selection = this.grid.getSelectionModel().getSelectedCell();
					if (selection == undefined) {
						return;
					}
					this.grid.getSelectionModel().clearSelections();
		            this.grid.stopEditing();					
					var record = ds.getAt(selection[0]);
					ds.remove(record);
					ds.commitChanges();           
				}.bind(this)
			}]);			
		
			// Basic Dialog
			this.dialog = new Ext.Window({ 
				autoScroll: true,
				autoCreate: true, 
				title: ORYX.I18N.PropertyWindow.complex, 
				height: 350, 
				width: dialogWidth, 
				modal:true,
				collapsible:false,
				fixedcenter: true, 
				shadow:true, 
				proxyDrag: true,
				keys:[{
					key: 27,
					fn: function(){
						this.dialog.hide
					}.bind(this)
				}],
				items:[toolbar, this.grid],
				bodyStyle:"background-color:#FFFFFF",
				buttons: [{
	                text: ORYX.I18N.PropertyWindow.ok,
	                handler: function(){
	                    this.grid.stopEditing();	
						// store dialog input
						this.data = this.buildValue();
						this.dialog.hide()
	                }.bind(this)
	            }, {
	                text: ORYX.I18N.PropertyWindow.cancel,
	                handler: function(){
	                	this.dialog.hide()
	                }.bind(this)
	            }]
			});		
				
			this.dialog.on(Ext.apply({}, this.dialogListeners, {
	       		scope:this
	        }));
		
			this.dialog.show();	
		
	
			this.grid.on('beforeedit', 	this.beforeEdit, 	this, true);
			this.grid.on('afteredit', 	this.afterEdit, 	this, true);
			
			this.grid.render();			
	    
		/*} else {
			this.dialog.show();		
		}*/
		
	}
});





Ext.form.ComplexTextField = Ext.extend(Ext.form.TriggerField,  {

	defaultAutoCreate : {tag: "textarea", rows:1, style:"height:16px;overflow:hidden;" },

    /**
     * If the trigger was clicked a dialog has to be opened
     * to enter the values for the complex property.
     */
    onTriggerClick : function(){
		
        if(this.disabled){
            return;
        }	
		        
		var grid = new Ext.form.TextArea({
	        anchor		: '100% 100%',
			value		: this.value,
			listeners	: {
				focus: function(){
					this.facade.disableEvent(ORYX.CONFIG.EVENT_KEYDOWN);
				}.bind(this)
			}
		});
		
		
		// Basic Dialog
		var dialog = new Ext.Window({ 
			layout		: 'anchor',
			autoCreate	: true, 
			title		: ORYX.I18N.PropertyWindow.text, 
			height		: 500, 
			width		: 500, 
			modal		: true,
			collapsible	: false,
			fixedcenter	: true, 
			shadow		: true, 
			proxyDrag	: true,
			keys:[{
				key	: 27,
				fn	: function(){
						dialog.hide()
				}.bind(this)
			}],
			items		:[grid],
			listeners	:{
				hide: function(){
					this.fireEvent('dialogClosed', this.value);
					//this.focus.defer(10, this);
					dialog.destroy();
				}.bind(this)				
			},
			buttons		: [{
                text: ORYX.I18N.PropertyWindow.ok,
                handler: function(){	 
					// store dialog input
					var value = grid.getValue();
					this.setValue(value);
					
					this.dataSource.getAt(this.row).set('value', value)
					this.dataSource.commitChanges()

					dialog.hide()
                }.bind(this)
            }, {
                text: ORYX.I18N.PropertyWindow.cancel,
                handler: function(){
					this.setValue(this.value);
                	dialog.hide();
                }.bind(this)
            }]
		});		
				
		dialog.show();		
		grid.render();

		this.grid.stopEditing();
		grid.focus( false, 100 );
		
	}
});

Ext.form.PromptField = Ext.extend(Ext.form.TriggerField,  {

	defaultAutoCreate : {tag: "textarea", rows:1, style:"height:16px;overflow:hidden;" },

    /**
     * If the trigger was clicked a dialog has to be opened
     * to enter the values for the complex property.
     */
	 
	 onTriggerClick : function(){
	 
	 if(this.disabled){
            return;
        }	
		

		var myReader = new Ext.data.JsonReader({root:'list'},[{name:'id',mapping:'id'},{name:'frase',mapping:'frase'}]);
				 
		var dataFromServer = new Ext.data.Store({
			proxy:new Ext.data.HttpProxy({url: location.protocol+'//'+location.host+'/designer/Data'}),// new Ext.data.HttpProxy({url: '/ext/strutsORspringMVCReturningArray'}),
			reader: myReader
		});
		
		
				 
		var com= new Ext.grid.ColumnModel([
			new Ext.grid.RowNumberer(),
			{header: 'id', width: 40, sortable: true, dataIndex: 'id'},
			{header:'frase',width:200,sortable:true,dataIndex:'frase'}]);      
					   
		var myPagingToolbar = new Ext.PagingToolbar({
			pageSize: 10,
			displayInfo: true,
			displayMsg: '{2} registros encontrados. Mostrando {0} - {1}',
			emptyMsg: "No se encontraron registros",
			store: dataFromServer
		});
		
		var selModel = new Ext.grid.RowSelectionModel({
			singleSelect : true
		});
		        
		var grid = new Ext.grid.GridPanel({
			id:'prompt',
			anchor: '100% 100%',
			value		: this.value,
			store: dataFromServer,
			cm:com,
			autoScroll:true,
			layout:'fit',
			selModel: selModel,
			listeners	: {
				focus: function(){
					this.facade.disableEvent(ORYX.CONFIG.EVENT_KEYDOWN);
				}.bind(this)
			}
		});
		
		dataFromServer.load({params:{start:0, limit:10}});
		
		
		
		
		// Basic Dialog
		var dialog = new Ext.Window({ 
			layout		: 'anchor',
			autoCreate	: true, 
			title		: ORYX.I18N.PropertyWindow.text, 
			height		: 500, 
			width		: 500, 
			modal		: true,
			collapsible	: false,
			fixedcenter	: true, 
			shadow		: true, 
			proxyDrag	: true,
			keys:[{
				key	: 27,
				fn	: function(){
						dialog.hide()
				}.bind(this)
			}],
			items		:[grid],
			listeners	:{
				hide: function(){
					this.fireEvent('dialogClosed', this.value);
					//this.focus.defer(10, this);
					dialog.destroy();
				}.bind(this)				
			},
			buttons		: [{
                text: ORYX.I18N.PropertyWindow.ok,
                handler: function(){	 
					// store grid input
					var promptgrid = Ext.getCmp('prompt');
					var rows = promptgrid.getSelectionModel().getSelections();
					if(!rows.length)
					{//in case this fires with no selection
						alert(ORYX.I18N.PropertyWindow.norows);
						return;
					}
					var value = rows[0].get('frase');
					this.setValue(value);
					
					
					this.dataSource.getAt(this.row).set('value', value)
					this.dataSource.commitChanges()

					dialog.hide()
                }.bind(this)
            }, {
                text: ORYX.I18N.PropertyWindow.cancel,
                handler: function(){
					this.setValue(this.value);
                	dialog.hide()
                }.bind(this)
            }]
		});		
				
		dialog.show();		
		grid.render();

		this.grid.stopEditing();
		grid.focus( false, 100 );
	 
	 }
		
        
});

Ext.form.ComponentField = Ext.extend(Ext.form.TriggerField,  {

	defaultAutoCreate : {tag: "textarea", rows:1, style:"height:16px;overflow:hidden;" },

    /**
     * If the trigger was clicked a dialog has to be opened
     * to enter the values for the complex property.
     */
	 
	 onTriggerClick : function(){
	 
	 if(this.disabled){
            return;
        }	
		

		var myReader = new Ext.data.JsonReader({root:'list'},[{name:'id',mapping:'id'},{name:'frase',mapping:'frase'}]);
				 
		var dataFromServer = new Ext.data.Store({
			proxy:new Ext.data.HttpProxy({url: location.protocol+'//'+location.host+'/designer/Data'}),// new Ext.data.HttpProxy({url: '/ext/strutsORspringMVCReturningArray'}),
			reader: myReader
		});
		
		
				 
		var com= new Ext.grid.ColumnModel([
			new Ext.grid.RowNumberer(),
			{header: 'id', width: 40, sortable: true, dataIndex: 'id'},
			{header:'frase',width:200,sortable:true,dataIndex:'frase'}]);      
					   
		var myPagingToolbar = new Ext.PagingToolbar({
			pageSize: 10,
			displayInfo: true,
			displayMsg: '{2} registros encontrados. Mostrando {0} - {1}',
			emptyMsg: "No se encontraron registros",
			store: dataFromServer
		});
		
		var selModel = new Ext.grid.RowSelectionModel({
			singleSelect : true
		});
		        
		var grid = new Ext.grid.GridPanel({
			id:'prompt',
			anchor: '100% 100%',
			value		: this.value,
			store: dataFromServer,
			cm:com,
			autoScroll:true,
			layout:'fit',
			selModel: selModel,
			listeners	: {
				focus: function(){
					this.facade.disableEvent(ORYX.CONFIG.EVENT_KEYDOWN);
				}.bind(this)
			}
		});
		
		dataFromServer.load({params:{start:0, limit:10}});
		
		
		
		
		// Basic Dialog
		var dialog = new Ext.Window({ 
			layout		: 'anchor',
			autoCreate	: true, 
			title		: ORYX.I18N.PropertyWindow.text, 
			height		: 500, 
			width		: 500, 
			modal		: true,
			collapsible	: false,
			fixedcenter	: true, 
			shadow		: true, 
			proxyDrag	: true,
			keys:[{
				key	: 27,
				fn	: function(){
						dialog.hide()
				}.bind(this)
			}],
			items		:[grid],
			listeners	:{
				hide: function(){
					this.fireEvent('dialogClosed', this.value);
					//this.focus.defer(10, this);
					dialog.destroy();
				}.bind(this)				
			},
			buttons		: [{
                text: ORYX.I18N.PropertyWindow.ok,
                handler: function(){	 
					// store grid input
					var promptgrid = Ext.getCmp('prompt');
					var rows = promptgrid.getSelectionModel().getSelections();
					if(!rows.length)
					{//in case this fires with no selection
						alert(ORYX.I18N.PropertyWindow.norows);
						return;
					}
					var value = rows[0].get('frase');
					this.setValue(value);
					
					
					this.dataSource.getAt(this.row).set('value', value);
					this.dataSource.commitChanges();

					dialog.hide();
                }.bind(this)
            }, {
                text: ORYX.I18N.PropertyWindow.cancel,
                handler: function(){
					this.setValue(this.value);
                	dialog.hide()
                }.bind(this)
            }]
		});		
				
		dialog.show();		
		grid.render();

		this.grid.stopEditing();
		grid.focus( false, 100 );
	 
	 }
		
        
});

//Extended by Ana Paula (start)

Ext.form.UploadFileField = Ext.extend(Ext.form.TriggerField,  {
	/*defaultAutoCreate : {tag: "input", type:"text",  readonly: "readonly", style:"height:16px;overflow:hidden;" },*/
	defaultAutoCreate : {tag: "textarea", rows:1, readonly: "readonly", style:"height:16px;overflow:hidden;" },
	
	 /**
     * If the trigger was clicked a dialog has to be opened
     * to enter the values for the complex property.
     */
	 
	 onTriggerClick : function(){
	 
	 if(this.disabled){
            return;
        }	
	 
	 	//this.showUploadDialog(this);
	 var url = location.protocol + '//' + location.host + ORYX.CONFIG.FILE_PROPERTY_UPLOAD_URL +"?user="+ORYX.USERNAME+"&sparkName=" + sparkName + "&fileName=";
     //var serviceURL = location.protocol+'//'+location.host+'/designer/services/JsonEngineLogic?wsdl';
     var form = new Ext.form.FormPanel({
             baseCls : 'x-plain',
             labelWidth : 50,
             defaultType : 'textfield',  
             fileUpload: true,
             items : [ {
                     text : ORYX.I18N.FILE_PROPERTY.selectFileText,
                     style : 'font-size:12px;margin-bottom:10px;display:block;',
                     anchor : '100%',
                     xtype : 'label'
             }, {
                     fieldLabel : ORYX.I18N.FILE_PROPERTY.fileText,
                     name : 'subject',
                     inputType : 'file',                            
                     style : 'margin-bottom:10px;display:block;',
                     itemCls : 'ext_specific_window_overflow'
             }, {
                     xtype : 'textarea',
                     hideLabel : true,
                     name : 'msg',
                     anchor : '100% -63'
             }]
     });

     // Create the panel
     var fileDialog = new Ext.Window({
             autoCreate : true,
             layout : 'fit',
             plain : true,
             bodyStyle : 'padding:5px;',
             title : ORYX.I18N.FILE_PROPERTY.uploadTitle,
             height : 350,
             width : 500,
             modal : true,
             fixedcenter : true,
             shadow : true,
             proxyDrag : true,
             resizable : true,
             items : [ form ],
             listeners	:{
 				hide: function(){
 					this.fireEvent('dialogClosed', this.value); 					
 					fileDialog.destroy(true);
 		            delete fileDialog;
 				}.bind(this)				
 			},
             buttons : [ {
                     text : ORYX.I18N.FILE_PROPERTY.uploadButtonText,
                     handler : function() {
                    	 
                    	 var value = form.items.items[1].getValue();
                    	 
                    	 if(value != ''){
                    	 	 /* Arreglo para evitar el fakepath del chrome */
                    		 value = value.replace("C:\\fakepath\\", "");
                    		 
                    		 url = url + value;
                    		 
                             progressDialog = Ext.MessageBox.progress(ORYX.I18N.FILE_PROPERTY.uploadProgress);                                  
                            
                             window.setTimeout(function() {

                                     var fileString = form.items.items[2].getValue();
                                     Ext.Ajax.request({
                                             url : url,
                                             method : 'POST',
                                             form: form.getForm().id,
                                             params : {
                                                 /*data : form.items.items[1].getValue(),*/
                                                 user: ORYX.USERNAME,                                                 
                                                 fileName: value
                                             },
                                             // form: form.getForm().getEl().dom                                                    
                                             headers: {'Content-type':'multipart/form-data'},
                                             scope : this,
                                             success : function(request) {
                                                    /*
                                                     var uploadObject = Ext.decode(request.responseText);
                                                    
                                                     if (uploadObject.hasFailed) {
                                                             //alert(ORYX.I18N.YAWL.messageConversionFailed +importObject.warnings);
                                                     		alert("ERROR AL CARGARRRRRRRRRRRRRRRRRR");
                                                             progressDialog.hide();
                                                     }else{
                                                     	dialog.hide();
                                                     }
                                                     */  
                                             	//progressDialog.hide();
                                            	progressDialog.updateProgress(100, 'Successfully uploaded!');
                                     			progressDialog.hide.defer(2000,progressDialog);
                                             	filetoUploadName = form.items.items[1].getValue();
                                             	/* Arreglo para evitar el fakepath del chrome */
                                             	filetoUploadName = filetoUploadName.replace("C:\\fakepath\\", "");
                                             	/* Seteando el nuevo valor en el input */
                                             	this.setValue(filetoUploadName);
                                             	/* Seteando el nuevo valor en el div del grid */
                            					this.dataSource.getAt(this.row).set('value', filetoUploadName);
                            					this.dataSource.commitChanges();                         					
                            					
                                             	fileDialog.hide();  
                                             	
                                             }.bind(this),
                                             failure : function() {
                                                     progressDialog.hide();
                                                     Ext.Msg.alert(ORYX.I18N.FILE_PROPERTY.uploadFailed);
                                             }                                                    
                                     });

                             }.bind(this), 100);
                            
                             //dialog.hide();
                     }else{
                    	 // Avisar al usuario de que debe seleccionar un fichero
                    	 Ext.Msg.alert(ORYX.I18N.FILE_PROPERTY.mandatoryFile);
                     }
                     }.bind(this)
             }, {
                     text : ORYX.I18N.FILE_PROPERTY.closeButtonText,
                     handler : function() {

                     	fileDialog.hide();

                     }.bind(this)
             } ]
     });

     // Destroy the panel when hiding
     /*
     fileDialog.on('hide', function() {
     		fileDialog.destroy(true);
             delete fileDialog;
     });*/
       

     // Show the panel     
     fileDialog.show();  
     // Para ocultar el textfield y que sólo se vea el fileDialog
     this.grid.stopEditing();     

     // Adds the change event handler to
     form.items.items[1].getEl().dom.addEventListener('change',
                     function(evt) {
                             var reader = new FileReader();  
                             var file = evt.target.files[0];
                             //Bugfix for Chrome
                             
                             if(typeof(FileReader.prototype.addEventListener) === "function") {
                                     reader.addEventListener("loadend", function(evt) {                                              
                                             form.items.items[2].setValue(evt.target.result);
                                     }, false);
                                    
                             } else {
                                     reader.onload = function(evt) {                                        
                                             form.items.items[2].setValue(evt.target.result);
                                     };                                              
                             }
                             reader.readAsText(file,"UTF-8");
                     }, true);     
     
	 }
});

//Extended by Ana Paula (end)

//Extended by Ana Paula (start) - File Browser

Ext.form.FileBrowserPanel = Ext.extend(Ext.Panel, {
	/**
	 * @cfg {String} dataUrl The URL that is used to process data (required)
	 */	
	dataUrl: location.protocol + '//' + location.host + ORYX.CONFIG.FILE_PROPERTY_GET_FILES_URL +"?user="+ORYX.USERNAME+"&sparkName=" + sparkName,
	
	/**
	 * Called by Ext when instantiating
	 *
	 * @private
	 * @param {Object} config Configuration object
	 */
	initComponent: function () {
		var config;
		
		var store = new Ext.data.JsonStore({
			//url:this.dataUrl,
			url: location.protocol + '//' + location.host + ORYX.CONFIG.FILE_PROPERTY_GET_FILES_URL +"?user="+ORYX.USERNAME+"&sparkName=" + sparkName,			
			fields: [
				{name: 'name', type: 'string'},
				{name: 'size', type: 'int'},				
				{name: 'date_modified', convert: function(v){return new Date(v);}},
				{name: 'c_check', type:'boolean'}
			]
		});		

		// create a grid that displays files
		this.grid = new Ext.grid.EditorGridPanel({			
			//title: this.il8n.gridPanelHeaderText,
			border: false,
			stripeRows: true,
			enableDragDrop: true,
			trackMouseOver: true,
			ddGroup: 'fileMoveDD',
			colModel: new Ext.grid.ColumnModel([
				{
					header: this.il8n.gridColumnNameHeaderText,
					id: 'name',
					dataIndex: 'name',
					sortable: true,
					//editor:	new Ext.form.TextField({vtype: 'filename'})
				}, {
					header: this.il8n.gridColumnSizeHeaderText,
					dataIndex: 'size',
					sortable: true,
					renderer: Ext.util.Format.bytesToSi
				}, {
					header: this.il8n.gridColumnDateModifiedText,
					dataIndex: 'date_modified',
					sortable: true,
					renderer: Ext.util.Format.dateRenderer(this.il8n.displayDateFormat)
				},
				{	id : 'Ch',					
					header : this.il8n.gridColumnUsedText, 
					dataIndex : 'c_check',	
					width: 30,
					//editor:  new Ext.form.Checkbox({applyTo: 'local-states'})
					renderer : function(val){
						//alert(val);
						return  "<input type='checkbox' name=\"check[]\" " + (val ? " checked='checked'" : "") + "/>";
						//return "<input checked=\"checked\" name=\"check[]\" value=\""+val+"\" type=\"checkbox\"/>";
					}
				}				
			]),
			store: store,
			selModel: new Ext.grid.RowSelectionModel({
				listeners: {
					selectionchange: {
						fn: this.onGridSelectionChange,
						scope: this
					}
				}
			}),
			viewConfig: {
				forceFit: true,
				emptyText: this.il8n.noFilesText,
				getRowClass: function (record, rowIndex, rowParams, store) {
					return 'filebrowser-iconrow ' + record.get('row_class');
				}
			},
			listeners: {
				render: {
					fn: this.onGridRender
				},
				rowcontextmenu: {
					fn: this.onGridContextMenu,
					scope: this
				}/*,
				afteredit: {
					fn: this.onGridEditorAfterEdit,
					scope: this
				}*/,
				/*
				click : function(event,a){
						var element = event.target;
						var check = document.getElementsByName('check[]');
						var checkLength = check.length;

						for(var i=0; i < checkLength; i++){
							if(check[i].checked){
								alert(check[i].value);
							}
						}
				}*/
				click: {
					fn: this.onCheckChange,
					scope: this
				}
			}
		});


		// config
		config = Ext.apply(this.initialConfig, {
			layout: 'border',
			border: false,
			items: [{
				region: 'center',
				layout: 'card',
				activeItem: 0,				
				items: [
					this.grid					
				]
			}]
		});

		// appy the config
		Ext.apply(this, config);

		// Call parent (required)
		Ext.form.FileBrowserPanel.superclass.initComponent.apply(this, arguments);

		// flag indicating which 'viewMode' is selected
		// can be 'details' or 'thumbnails'
		this.viewMode = 'details';

		// install events
		this.addEvents(	
			
			/**
			 * @event beforedeletefile
			 * Fires before file will be deleted from the server,
			 * return false to cancel the event
			 *
			 * @param {Ext.form.FileBrowserPanel}	this
			 * @param {Ext.data.Record}			record	The record representing the file that will be deleted
			 */
			'beforedeletefile',

			/**
			 * @event beforedownloadfile
			 * Fires before file will be downloaded from the server,
			 * return false to cancel the event
			 *
			 * @param {Ext.form.FileBrowserPanel}	this
			 * @param {Ext.data.Record}			record	The record representing the file that will be downloaded
			 */
			'beforedownloadfile',			

			/**
			 * @event deletefile
			 * Fires when file(s) was/were successfully deleted
			 *
			 * @param {Ext.form.FileBrowserPanel}	this
			 * @param {Object}					opts	The options that were used for the original request
			 * @param {Object}					o		Decoded response body from the server
			 */
			'deletefile',			

			/**
			 * @event deletefilefailed
			 * Fires when deleting file(s) failed
			 *
			 * @param {Ext.form.FileBrowserPanel}	this
			 * @param {Object}					opts	The options that were used for the original request
			 * @param {Object}					o		Decoded response body from the server
			 */
			'deletefilefailed'
		);

	}, // eo function initComponent	

	/**
	 * Event handlers for when grid row is right-clicked
	 * Shows context menu
	 *
	 * @private
	 * @param	{Ext.grid.GridPanel}	grid		Grid panel that was right-clicked
	 * @param	{Integer}				rowIndex	Index of the selected row
	 * @param	{Ext.EventObject}		evt			Event object
	 * @returns	{Void}
	 */
	onGridContextMenu: function (grid, rowIndex, evt) {
		var contextMenu;

		evt.stopEvent();
		grid.getSelectionModel().selectRow(rowIndex);

		contextMenu = this.getGridContextMenu();
		contextMenu.rowIndex = rowIndex;
		contextMenu.showAt(evt.getXY());
	}, // eo function onGridContextMenu

		
	/**
	 * Event handler for when grid-specific contentmenu item is clicked
	 * Delegates actions for menu items to other methods
	 *
	 * @private
	 * @param 	{Ext.menu.Menu}		menu		he context menu
	 * @param	{Ext.menu.Item}		menuItem	The menu item that was clicked
	 * @param	{Ext.EventObject}	evt			Event object
	 * @returns	{Void}
	 */
	onGridContextMenuClick: function (menu, menuItem, evt) {
		var colIndex, labelEditor, record, el, records;
		switch (menuItem.cmd) {
		case 'delete':
			if (this.viewMode === 'details') {
				records = this.grid.getSelectionModel().getSelections();
			} 
			this.deleteFile(records);
			break;

		case 'download':
			if (this.viewMode === 'details') {
				record = this.grid.getSelectionModel().getSelected();
			}
			this.downloadFile(record);
			break;

		default:
			break;
		}
	}, // eo function onGridContextMenuClick	

	
	onCheckChange: function (event){
		var record = this.grid.getSelectionModel().getSelected();		
		// 1. Obtenemos el valor del check de esa fila	
		var isUsed = document.getElementById(event.target.id).checked;
		// 2. Obtenemos el nombre del archivo
		var fileName = record.data.name;
		// 3. Enviar petición al servidor para eliminar el archivo
		// de la lista de archivos usados para el spark
		// send request to server
		Ext.Ajax.request({
			url: location.protocol + '//' + location.host + ORYX.CONFIG.FILE_PROPERTY_MARK_UNUSED_FILES_URL +"?user="+ORYX.USERNAME+"&sparkName=" + sparkName + "&fileIsUsed=" + isUsed + "&fileToMark=" + fileName,
			callback: this.actionCallback,
			scope: this,
			params: params
		});
	},

	/**
	 * Event handler for when selection in the grid changes
	 * En- or disables buttons in the toolbar depending on selection in the grid
	 *
	 * @private
	 * @param	{Ext.grid.RowSelectionModel} sm The selection model
	 * @returns	{Void}
	 */
	onGridSelectionChange: function (sm) {
		/*
		if (sm.hasSelection()) {
			this.enableGridToolbarButtons();
		} else {
			this.disableGridToolbarButtons();
		}*/
	}, // eo function onGridSelectionChange

	
	/**
	 * Event handler for when the grid is about to load new data
	 * Appends the folder path of the selected node to the request
	 *
	 * @private
	 * @param	{Ext.data.JsonStore}	store	The store object
	 * @param	{Object}				opts	Loading options
	 * @returns	{Void}
	 */
	onGridStoreBeforeLoad: function (store, opts) {
		
	}, // eo function onGridStoreBeforeLoad
	
	onLoadGridStore: function(){
		var prueba = 'loaded';
		alert(prueba);
	},
	/**
	 * Event handler when the grid is rendered
	 * Loads the store
	 *
	 * @private
	 * @param	{Ext.grid.GridPanel} grid The grid panel
	 * @returns	{Void}
	 */
	onGridRender: function (grid) {		
		grid.getStore().load({callback:this.onLoadGridStore});				
	}, // eo function onGridRender

			
	/**
	 * Gets and lazy creates context menu for file grid
	 *
	 * @private
	 * @returns {Ext.menu.Menu} Context menu
	 */
	getGridContextMenu: function () {

		if (!this.gridContextMenu) {
			this.gridContextMenu = new Ext.menu.Menu({
				items: [{
					text: this.il8n.deleteText,
					cmd: 'delete',
					iconCls: 'filebrowser-icon-deletefile'
				}, {
					text: this.il8n.downloadText,
					cmd: 'download',
					iconCls: 'filebrowser-icon-downloadfile'
				}],
				listeners: {
					click: {
						fn: this.onGridContextMenuClick,
						scope: this
					}
				}
			});
		}

		return this.gridContextMenu;
	}, // eo function getGridContextMenu
		

	/**
	 * Callback that handles all actions performed on the server (delete, download etc.)
	 * Called when Ajax request finishes, regardless if this was a success or not
	 *
	 * @private
	 * @param	{Object}	opts		The options that were used for the original request
	 * @param	{Boolean}	success		If the request succeded
	 * @param	{Object}	response	The XMLHttpRequest object containing the response data
	 * @returns	{Void}
	 */
	actionCallback: function (opts, success, response) {
		var o = {}, store, record;

		// check if request was successful
		if (true !== success) {
			Ext.Msg.show({
				title: this.il8n.actionRequestFailureTitleText,
				msg: this.il8n.actionRequestFailureMsgText,
				buttons: Ext.Msg.OK,
				icon: Ext.Msg.ERROR,
				closable: false
			});
			return;
		}

		// decode response 
		// show error message in case of failure
		try {
			o = Ext.decode(response.responseText);
		} catch (e) {
			Ext.Msg.show({
				title: this.il8n.actionResponseFailureTitleText,
				msg: this.il8n.actionResponseFailureMsgText,
				buttons: Ext.Msg.OK,
				icon: Ext.Msg.ERROR,
				closable: false
			});
		}

		// check if server reports all went well
		// handle success/failure accordingly
		if (true === o.success) {
			switch (opts.params.action) {		

			case 'delete-file':
				// fire deletefile event
				if (true !== this.eventsSuspended) {
					this.fireEvent('deletefile', this, opts, o);
				}

				// delete record(s) from the grid
				store = this.grid.getStore();
				Ext.each(o.data, function (item, index, allItems) {
					//record = store.getById(item.recordId);
					record = store.getAt(store.find('name',item.name));
					store.remove(record);
				});
				break;			

			default:
				break;
			}
		} else {
			switch (opts.params.action) {				

			case 'delete-file':
				// fire deletefilefailed event
				if (true !== this.eventsSuspended) {
					this.fireEvent('deletefilefailed', this, opts, o);
				}

				// delete successfully moved record(s) from the grid
				store = this.grid.getStore();
				Ext.each(o.data.successful, function (item, index, allItems) {
					record = store.getAt(store.find('name',item.name));
					store.remove(record);
				});
				break;			

			default:
				break;
			}
		}

	}, // eo function actionCallback	
	

	/**
	 * Deletes file from the server
	 *
	 * @private
	 * @param	{Array}	files Array of Ext.data.Record objects representing the files that need to be deleted
	 * @returns	{Void}
	 */
	deleteFile: function (files) {
		var params, folder, dialogTitle, dialogMsg;
		// fire beforedeletefile event
		if (true !== this.eventsSuspended &&
		   false === this.fireEvent('beforedeletefile', this, files)) {
			return;
		}

		// set request parameters
		params = {
			action: 'delete-file'
		};

		var filesToDelete = "";
		
		Ext.each(files, function (item, index, allItems) {
			filesToDelete += files[0].data.name + ",";			
		});
		params['filesToDelete'] = filesToDelete.substring(0,filesToDelete.length-1);

		// prepare confirmation texts depending on amount of files
		dialogTitle = this.il8n.confirmDeleteSingleFileTitleText;
		dialogMsg = String.format(this.il8n.confirmDeleteSingleFileMsgText, files[0].get('name'));
		if (files.length > 1) {
			dialogTitle = this.il8n.confirmDeleteMultipleFileTitleText;
			dialogMsg = String.format(this.il8n.confirmDeleteMultipleFileMsgText, files.length);
		}

		// confirm removal
		Ext.Msg.show({
			title: dialogTitle,
			msg: dialogMsg,
			buttons: Ext.Msg.YESNO,
			icon: Ext.Msg.QUESTION,
			closable: false,
			scope: this,
			fn: function (buttonId) {
				if (buttonId === 'yes') {
					// send request to server
					Ext.Ajax.request({
						url: location.protocol + '//' + location.host + ORYX.CONFIG.FILE_PROPERTY_REMOVE_FILES_URL +"?user="+ORYX.USERNAME+"&sparkName=" + sparkName,
						callback: this.actionCallback,
						scope: this,
						params: params
					});
				}
			}
		});
	}, // eo function deleteFile

	/**
	 * Download a file from the server	 
	 * @see http://filetree.extjs.eu/
	 *
	 * @private
	 * @param	{Ext.data.Record} record Record representing the file that needs to be downloaded
	 * @returns	{Void}
	 */
	downloadFile: function (record) {
		var id, frame, form, hidden, callback;
		// fire beforedownloadfile event
		if (true !== this.eventsSuspended &&
		   false === this.fireEvent('beforedownloadfile', this, record)) {
			return;
		}

		// generate a new unique id
		id = Ext.id();

		// create a new iframe element
		frame = document.createElement('iframe');
		frame.id = id;
		frame.name = id;
		frame.className = 'x-hidden';

		// use blank src for Internet Explorer
		if (Ext.isIE) {
			frame.src = Ext.SSL_SECURE_URL;
		}

		// append the frame to the document
		document.body.appendChild(frame);

		// also set the name for Internet Explorer
		if (Ext.isIE) {
			document.frames[id].name = id;
		}

		//  create a new form element
		form = Ext.DomHelper.append(document.body, {
			tag: 'form',
			method: 'post',
			action: location.protocol + '//' + location.host + ORYX.CONFIG.FILE_PROPERTY_DOWNLOAD_FILE_URL,
			target: id
		});

		// create hidden input element with the 'sparkName'
		hidden = document.createElement('input');
		hidden.type = 'hidden';
		hidden.name = 'sparkName';
		hidden.value = sparkName;
		form.appendChild(hidden);
		
		// create hidden input element with the 'userName'
		hidden = document.createElement('input');
		hidden.type = 'hidden';
		hidden.name = 'user';
		hidden.value = ORYX.USERNAME;
		form.appendChild(hidden);

		// create another hidden element that holds the path of the file to download
		hidden = document.createElement('input');
		hidden.type = 'hidden';
		hidden.name = 'fileName';
		//hidden.value = this.getSelectedFilePath();
		hidden.value = this.grid.getSelectionModel().getSelected().data.name;
		form.appendChild(hidden);

		// create a callback function that does some cleaning afterwards
		callback = function () {
			Ext.EventManager.removeListener(frame, 'load', callback, this);
			setTimeout(function () {
				document.body.removeChild(form);
			}, 100);
			setTimeout(function () {
				document.body.removeChild(frame);
			}, 110);
		};

		// attach callback and submit the form
		Ext.EventManager.on(frame, 'load', callback, this);
		form.submit();
	},

	
	/**
	 * Refreshes the grid with data from the server
	 *
	 * @returns	{Void}
	 */
	refreshGrid: function () {
		this.grid.getStore().load();
	}

}); // eo extend

// register xtype
Ext.reg('filebrowserpanel', Ext.form.FileBrowserPanel);

/**
 * LabelEditor
 * Used for editing the labels of the thumbnails
 * Code from ExtJS example website
 *
 * @class	Ext.form.FileBrowserPanel.LabelEditor
 * @extends	Ext.Editor
 */
Ext.form.FileBrowserPanel.LabelEditor = Ext.extend(Ext.Editor, {
	alignment: 'tl-tl',
	hideEl: false,
	cls: 'x-small-editor',
	shim: false,
	ignoreNoChange: true,
	completeOnEnter: true,
	cancelOnEsc: true,
	labelSelector: 'span.x-editable',

	constructor: function (cfg, field) {
		Ext.form.FileBrowserPanel.LabelEditor.superclass.constructor.call(this,
			field || new Ext.form.TextField({
				allowBlank: false,
				growMin: 90,
				growMax: 240,
				grow: true,
				selectOnFocus: true,
				vtype: 'filename'
			}), cfg
		);
	},

	init: function (view) {
		this.view = view;
		view.on('render', this.initEditor, this);
	},

	initEditor: function () {
		this.view.on({
			scope: this,
			containerclick: this.doBlur,
			click: this.doBlur
		});
		this.view.getEl().on('mousedown', this.onMouseDown, this, {delegate: this.labelSelector});
	},

	onMouseDown: function (evt, target) {
		if (!evt.ctrlKey && !evt.shiftKey) {
			var item, record;
			item = this.view.findItemFromChild(target);
			evt.stopEvent();
			record = this.view.store.getAt(this.view.indexOf(item));
			this.startEdit(target, record.data[this.dataIndex]);
			this.activeRecord = record;
		} else {
			evt.preventDefault();
		}
	},

	doBlur: function () {
		if (this.editing) {
			this.field.blur();
		}
	}
}); // eo extend

/**
 * Strings for internationalization
 */
Ext.form.FileBrowserPanel.prototype.il8n = {
	displayDateFormat: 'd/m/Y H:i:s',
	newText: 'New',
	renameText: 'Rename',
	deleteText: 'Delete',
	uploadText: 'Upload',
	downloadText: 'Download',
	viewsText: 'Views',
	detailsText: 'Details',
	thumbnailsText: 'Thumbnails',
	newFolderText: 'New-Folder',
	noFilesText: 'No files to display',
	gridPanelHeaderText: 'Files',
	gridColumnNameHeaderText: 'Name',
	gridColumnSizeHeaderText: 'Size',
	gridColumnTypeHeaderText: 'Type',
	gridColumnDateModifiedText: 'Date Modified',
	gridColumnUsedText: 'Used',
	confirmDeleteSingleFileTitleText: 'Confirm file delete',
	confirmDeleteSingleFileMsgText: "Are you sure you want to delete '{0}'?",
	confirmDeleteMultipleFileTitleText: 'Confirm multiple file delete',
	confirmDeleteMultipleFileMsgText: "Are you sure you want to delete these {0} files?",	
	actionRequestFailureTitleText: 'Oh dear..',
	actionRequestFailureMsgText: "Tt seems like we're having problems. We can't send your request until we solve this. Sorry.",
	actionResponseFailureTitleText: 'PANIC!!',
	actionResponseFailureMsgText: 'Try again later please.'
};

/**
 * Additional Format function(s) to use
 */
Ext.apply(Ext.util.Format, {
	/**
	 * Format filesize to human readable format
	 * Also deals with filesizes in units larger then MegaBytes
	 *
	 * @param	{Integer}	size	Filesize in bytes
	 * @returns	{String}			Formatted filesize
	 */
	bytesToSi: function (size) {
		if (typeof size === 'number' && size > 0) {
			var s, e, r;
			s = ['b', 'Kb', 'Mb', 'Gb', 'Tb', 'Pb', 'Eb', 'Zb', 'Yb'];
			e = Math.floor(Math.log(size) / Math.log(1024));
			r = size / Math.pow(1024, e);
			if (Math.round(r.toFixed(2)) !== r.toFixed(2)) {
				r = r.toFixed(2);
			}
			return r + ' ' + s[e];
		} else {
			return '0 b';
		}
	}
}); // eo apply
// eof

//Extended by Ana Paula (end)

Ext.form.SelectFileBrowser = Ext.extend(Ext.Panel, {
	/**
	 * @cfg {String} dataUrl The URL that is used to process data (required)
	 */	
	dataUrl: location.protocol + '//' + location.host + ORYX.CONFIG.FILE_PROPERTY_GET_FILES_URL +"?user="+ORYX.USERNAME+"&sparkName=" + sparkName,
	
	/**
	 * Called by Ext when instantiating
	 *
	 * @private
	 * @param {Object} config Configuration object
	 */
	initComponent: function () {
		var config;
		
		var store = new Ext.data.JsonStore({
			//url:this.dataUrl,
			url: location.protocol + '//' + location.host + ORYX.CONFIG.FILE_PROPERTY_GET_FILES_URL +"?user="+ORYX.USERNAME+"&sparkName=" + sparkName,			
			fields: [
				{name: 'name', type: 'string'},
				{name: 'size', type: 'int'},				
				{name: 'date_modified', convert: function(v){return new Date(v);}}				
			]
		});		

		// create a grid that displays files
		this.grid = new Ext.grid.EditorGridPanel({			
			//title: this.il8n.gridPanelHeaderText,
			border: false,
			stripeRows: true,
			enableDragDrop: true,
			trackMouseOver: true,
			ddGroup: 'fileMoveDD',
			colModel: new Ext.grid.ColumnModel([
				{
					header: this.il8n.gridColumnNameHeaderText,
					id: 'name',
					dataIndex: 'name',
					sortable: true,
					//editor:	new Ext.form.TextField({vtype: 'filename'})
				}, {
					header: this.il8n.gridColumnSizeHeaderText,
					dataIndex: 'size',
					sortable: true,
					renderer: Ext.util.Format.bytesToSi
				}, {
					header: this.il8n.gridColumnDateModifiedText,
					dataIndex: 'date_modified',
					sortable: true,
					renderer: Ext.util.Format.dateRenderer(this.il8n.displayDateFormat)
				}							
			]),
			store: store,
			selModel: new Ext.grid.RowSelectionModel({
				listeners: {
					selectionchange: {
						fn: this.onGridSelectionChange,
						scope: this
					}
				}
			}),
			viewConfig: {
				forceFit: true,
				emptyText: this.il8n.noFilesText,
				getRowClass: function (record, rowIndex, rowParams, store) {
					return 'filebrowser-iconrow ' + record.get('row_class');
				}
			},
			listeners: {
				render: {
					fn: this.onGridRender
				},
				rowcontextmenu: {
					fn: this.onGridContextMenu,
					scope: this
				}/*,
				afteredit: {
					fn: this.onGridEditorAfterEdit,
					scope: this
				}*/,
				/*
				click : function(event,a){
						var element = event.target;
						var check = document.getElementsByName('check[]');
						var checkLength = check.length;

						for(var i=0; i < checkLength; i++){
							if(check[i].checked){
								alert(check[i].value);
							}
						}
				}*/
				click: {
					fn: this.onCheckChange,
					scope: this
				}
			}
		});


		// config
		config = Ext.apply(this.initialConfig, {
			layout: 'border',
			border: false,
			items: [{
				region: 'center',
				layout: 'card',
				activeItem: 0,				
				items: [
					this.grid					
				]
			}]
		});

		// appy the config
		Ext.apply(this, config);

		// Call parent (required)
		Ext.form.FileBrowserPanel.superclass.initComponent.apply(this, arguments);

		// flag indicating which 'viewMode' is selected
		// can be 'details' or 'thumbnails'
		this.viewMode = 'details';

		// install events
		this.addEvents(	
			
			/**
			 * @event beforedeletefile
			 * Fires before file will be deleted from the server,
			 * return false to cancel the event
			 *
			 * @param {Ext.form.FileBrowserPanel}	this
			 * @param {Ext.data.Record}			record	The record representing the file that will be deleted
			 */
			'beforedeletefile',

			/**
			 * @event beforedownloadfile
			 * Fires before file will be downloaded from the server,
			 * return false to cancel the event
			 *
			 * @param {Ext.form.FileBrowserPanel}	this
			 * @param {Ext.data.Record}			record	The record representing the file that will be downloaded
			 */
			'beforedownloadfile',			

			/**
			 * @event deletefile
			 * Fires when file(s) was/were successfully deleted
			 *
			 * @param {Ext.form.FileBrowserPanel}	this
			 * @param {Object}					opts	The options that were used for the original request
			 * @param {Object}					o		Decoded response body from the server
			 */
			'deletefile',			

			/**
			 * @event deletefilefailed
			 * Fires when deleting file(s) failed
			 *
			 * @param {Ext.form.FileBrowserPanel}	this
			 * @param {Object}					opts	The options that were used for the original request
			 * @param {Object}					o		Decoded response body from the server
			 */
			'deletefilefailed',
			'clickfile'
		);

	}, // eo function initComponent	

	/**
	 * Event handlers for when grid row is right-clicked
	 * Shows context menu
	 *
	 * @private
	 * @param	{Ext.grid.GridPanel}	grid		Grid panel that was right-clicked
	 * @param	{Integer}				rowIndex	Index of the selected row
	 * @param	{Ext.EventObject}		evt			Event object
	 * @returns	{Void}
	 */
	onGridContextMenu: function (grid, rowIndex, evt) {
		var contextMenu;

		evt.stopEvent();
		grid.getSelectionModel().selectRow(rowIndex);

		contextMenu = this.getGridContextMenu();
		contextMenu.rowIndex = rowIndex;
		contextMenu.showAt(evt.getXY());
	}, // eo function onGridContextMenu

		
	/**
	 * Event handler for when grid-specific contentmenu item is clicked
	 * Delegates actions for menu items to other methods
	 *
	 * @private
	 * @param 	{Ext.menu.Menu}		menu		he context menu
	 * @param	{Ext.menu.Item}		menuItem	The menu item that was clicked
	 * @param	{Ext.EventObject}	evt			Event object
	 * @returns	{Void}
	 */
	onGridContextMenuClick: function (menu, menuItem, evt) {
		var colIndex, labelEditor, record, el, records;
		switch (menuItem.cmd) {
		case 'delete':
			if (this.viewMode === 'details') {
				records = this.grid.getSelectionModel().getSelections();
			} 
			this.deleteFile(records);
			break;

		case 'download':
			if (this.viewMode === 'details') {
				record = this.grid.getSelectionModel().getSelected();
			}
			this.downloadFile(record);
			break;

		default:
			break;
		}
	}, // eo function onGridContextMenuClick	

	
	onCheckChange: function (event){		
		var record = this.grid.getSelectionModel().getSelected();	
		this.fireEvent('clickfile', this, record);
		this.ownerCt.hide();
	},

	/**
	 * Event handler for when selection in the grid changes
	 * En- or disables buttons in the toolbar depending on selection in the grid
	 *
	 * @private
	 * @param	{Ext.grid.RowSelectionModel} sm The selection model
	 * @returns	{Void}
	 */
	onGridSelectionChange: function (sm) {
		/*
		if (sm.hasSelection()) {
			this.enableGridToolbarButtons();
		} else {
			this.disableGridToolbarButtons();
		}*/
	}, // eo function onGridSelectionChange

	
	/**
	 * Event handler for when the grid is about to load new data
	 * Appends the folder path of the selected node to the request
	 *
	 * @private
	 * @param	{Ext.data.JsonStore}	store	The store object
	 * @param	{Object}				opts	Loading options
	 * @returns	{Void}
	 */
	onGridStoreBeforeLoad: function (store, opts) {
		
	}, // eo function onGridStoreBeforeLoad
	
	onLoadGridStore: function(){
		var prueba = 'loaded';
		alert(prueba);
	},
	/**
	 * Event handler when the grid is rendered
	 * Loads the store
	 *
	 * @private
	 * @param	{Ext.grid.GridPanel} grid The grid panel
	 * @returns	{Void}
	 */
	onGridRender: function (grid) {		
		grid.getStore().load({callback:this.onLoadGridStore});				
	}, // eo function onGridRender

			
	/**
	 * Gets and lazy creates context menu for file grid
	 *
	 * @private
	 * @returns {Ext.menu.Menu} Context menu
	 */
	getGridContextMenu: function () {

		if (!this.gridContextMenu) {
			this.gridContextMenu = new Ext.menu.Menu({
				items: [{
					text: this.il8n.deleteText,
					cmd: 'delete',
					iconCls: 'filebrowser-icon-deletefile'
				}, {
					text: this.il8n.downloadText,
					cmd: 'download',
					iconCls: 'filebrowser-icon-downloadfile'
				}],
				listeners: {
					click: {
						fn: this.onGridContextMenuClick,
						scope: this
					}
				}
			});
		}

		return this.gridContextMenu;
	}, // eo function getGridContextMenu
		

	/**
	 * Callback that handles all actions performed on the server (delete, download etc.)
	 * Called when Ajax request finishes, regardless if this was a success or not
	 *
	 * @private
	 * @param	{Object}	opts		The options that were used for the original request
	 * @param	{Boolean}	success		If the request succeded
	 * @param	{Object}	response	The XMLHttpRequest object containing the response data
	 * @returns	{Void}
	 */
	actionCallback: function (opts, success, response) {
		var o = {}, store, record;

		// check if request was successful
		if (true !== success) {
			Ext.Msg.show({
				title: this.il8n.actionRequestFailureTitleText,
				msg: this.il8n.actionRequestFailureMsgText,
				buttons: Ext.Msg.OK,
				icon: Ext.Msg.ERROR,
				closable: false
			});
			return;
		}

		// decode response 
		// show error message in case of failure
		try {
			o = Ext.decode(response.responseText);
		} catch (e) {
			Ext.Msg.show({
				title: this.il8n.actionResponseFailureTitleText,
				msg: this.il8n.actionResponseFailureMsgText,
				buttons: Ext.Msg.OK,
				icon: Ext.Msg.ERROR,
				closable: false
			});
		}

		// check if server reports all went well
		// handle success/failure accordingly
		if (true === o.success) {
			switch (opts.params.action) {		

			case 'delete-file':
				// fire deletefile event
				if (true !== this.eventsSuspended) {
					this.fireEvent('deletefile', this, opts, o);
				}

				// delete record(s) from the grid
				store = this.grid.getStore();
				Ext.each(o.data, function (item, index, allItems) {
					//record = store.getById(item.recordId);
					record = store.getAt(store.find('name',item.name));
					store.remove(record);
				});
				break;			

			default:
				break;
			}
		} else {
			switch (opts.params.action) {				

			case 'delete-file':
				// fire deletefilefailed event
				if (true !== this.eventsSuspended) {
					this.fireEvent('deletefilefailed', this, opts, o);
				}

				// delete successfully moved record(s) from the grid
				store = this.grid.getStore();
				Ext.each(o.data.successful, function (item, index, allItems) {
					record = store.getAt(store.find('name',item.name));
					store.remove(record);
				});
				break;			

			default:
				break;
			}
		}

	}, // eo function actionCallback	
	

	/**
	 * Deletes file from the server
	 *
	 * @private
	 * @param	{Array}	files Array of Ext.data.Record objects representing the files that need to be deleted
	 * @returns	{Void}
	 */
	deleteFile: function (files) {
		var params, folder, dialogTitle, dialogMsg;
		// fire beforedeletefile event
		if (true !== this.eventsSuspended &&
		   false === this.fireEvent('beforedeletefile', this, files)) {
			return;
		}

		// set request parameters
		params = {
			action: 'delete-file'
		};

		var filesToDelete = "";
		
		Ext.each(files, function (item, index, allItems) {
			filesToDelete += files[0].data.name + ",";			
		});
		params['filesToDelete'] = filesToDelete.substring(0,filesToDelete.length-1);

		// prepare confirmation texts depending on amount of files
		dialogTitle = this.il8n.confirmDeleteSingleFileTitleText;
		dialogMsg = String.format(this.il8n.confirmDeleteSingleFileMsgText, files[0].get('name'));
		if (files.length > 1) {
			dialogTitle = this.il8n.confirmDeleteMultipleFileTitleText;
			dialogMsg = String.format(this.il8n.confirmDeleteMultipleFileMsgText, files.length);
		}

		// confirm removal
		Ext.Msg.show({
			title: dialogTitle,
			msg: dialogMsg,
			buttons: Ext.Msg.YESNO,
			icon: Ext.Msg.QUESTION,
			closable: false,
			scope: this,
			fn: function (buttonId) {
				if (buttonId === 'yes') {
					// send request to server
					Ext.Ajax.request({
						url: location.protocol + '//' + location.host + ORYX.CONFIG.FILE_PROPERTY_REMOVE_FILES_URL +"?user="+ORYX.USERNAME+"&sparkName=" + sparkName,
						callback: this.actionCallback,
						scope: this,
						params: params
					});
				}
			}
		});
	}, // eo function deleteFile

	/**
	 * Download a file from the server	 
	 * @see http://filetree.extjs.eu/
	 *
	 * @private
	 * @param	{Ext.data.Record} record Record representing the file that needs to be downloaded
	 * @returns	{Void}
	 */
	downloadFile: function (record) {
		var id, frame, form, hidden, callback;
		// fire beforedownloadfile event
		if (true !== this.eventsSuspended &&
		   false === this.fireEvent('beforedownloadfile', this, record)) {
			return;
		}

		// generate a new unique id
		id = Ext.id();

		// create a new iframe element
		frame = document.createElement('iframe');
		frame.id = id;
		frame.name = id;
		frame.className = 'x-hidden';

		// use blank src for Internet Explorer
		if (Ext.isIE) {
			frame.src = Ext.SSL_SECURE_URL;
		}

		// append the frame to the document
		document.body.appendChild(frame);

		// also set the name for Internet Explorer
		if (Ext.isIE) {
			document.frames[id].name = id;
		}

		//  create a new form element
		form = Ext.DomHelper.append(document.body, {
			tag: 'form',
			method: 'post',
			action: location.protocol + '//' + location.host + ORYX.CONFIG.FILE_PROPERTY_DOWNLOAD_FILE_URL,
			target: id
		});

		// create hidden input element with the 'sparkName'
		hidden = document.createElement('input');
		hidden.type = 'hidden';
		hidden.name = 'sparkName';
		hidden.value = sparkName;
		form.appendChild(hidden);
		
		// create hidden input element with the 'userName'
		hidden = document.createElement('input');
		hidden.type = 'hidden';
		hidden.name = 'user';
		hidden.value = ORYX.USERNAME;
		form.appendChild(hidden);

		// create another hidden element that holds the path of the file to download
		hidden = document.createElement('input');
		hidden.type = 'hidden';
		hidden.name = 'fileName';
		//hidden.value = this.getSelectedFilePath();
		hidden.value = this.grid.getSelectionModel().getSelected().data.name;
		form.appendChild(hidden);

		// create a callback function that does some cleaning afterwards
		callback = function () {
			Ext.EventManager.removeListener(frame, 'load', callback, this);
			setTimeout(function () {
				document.body.removeChild(form);
			}, 100);
			setTimeout(function () {
				document.body.removeChild(frame);
			}, 110);
		};

		// attach callback and submit the form
		Ext.EventManager.on(frame, 'load', callback, this);
		form.submit();
	},

	
	/**
	 * Refreshes the grid with data from the server
	 *
	 * @returns	{Void}
	 */
	refreshGrid: function () {
		this.grid.getStore().load();
	}

}); // eo extend


//register xtype
Ext.reg('selectfilebrowser', Ext.form.SelectFileBrowser);


/**
 * LabelEditor
 * Used for editing the labels of the thumbnails
 * Code from ExtJS example website
 *
 * @class	Ext.form.FileBrowserPanel.LabelEditor
 * @extends	Ext.Editor
 */
Ext.form.SelectFileBrowser.LabelEditor = Ext.extend(Ext.Editor, {
	alignment: 'tl-tl',
	hideEl: false,
	cls: 'x-small-editor',
	shim: false,
	ignoreNoChange: true,
	completeOnEnter: true,
	cancelOnEsc: true,
	labelSelector: 'span.x-editable',

	constructor: function (cfg, field) {
		Ext.form.FileBrowserPanel.LabelEditor.superclass.constructor.call(this,
			field || new Ext.form.TextField({
				allowBlank: false,
				growMin: 90,
				growMax: 240,
				grow: true,
				selectOnFocus: true,
				vtype: 'filename'
			}), cfg
		);
	},

	init: function (view) {
		this.view = view;
		view.on('render', this.initEditor, this);
	},

	initEditor: function () {
		this.view.on({
			scope: this,
			containerclick: this.doBlur,
			click: this.doBlur
		});
		this.view.getEl().on('mousedown', this.onMouseDown, this, {delegate: this.labelSelector});
	},

	onMouseDown: function (evt, target) {
		if (!evt.ctrlKey && !evt.shiftKey) {
			var item, record;
			item = this.view.findItemFromChild(target);
			evt.stopEvent();
			record = this.view.store.getAt(this.view.indexOf(item));
			this.startEdit(target, record.data[this.dataIndex]);
			this.activeRecord = record;
		} else {
			evt.preventDefault();
		}
	},

	doBlur: function () {
		if (this.editing) {
			this.field.blur();
		}
	}
}); // eo extend

/**
 * Strings for internationalization
 */
Ext.form.SelectFileBrowser.prototype.il8n = {
	displayDateFormat: 'd/m/Y H:i:s',
	newText: 'New',
	renameText: 'Rename',
	deleteText: 'Delete',
	uploadText: 'Upload',
	downloadText: 'Download',
	viewsText: 'Views',
	detailsText: 'Details',
	thumbnailsText: 'Thumbnails',
	newFolderText: 'New-Folder',
	noFilesText: 'No files to display',
	gridPanelHeaderText: 'Files',
	gridColumnNameHeaderText: 'Name',
	gridColumnSizeHeaderText: 'Size',
	gridColumnTypeHeaderText: 'Type',
	gridColumnDateModifiedText: 'Date Modified',
	gridColumnUsedText: 'Used',
	confirmDeleteSingleFileTitleText: 'Confirm file delete',
	confirmDeleteSingleFileMsgText: "Are you sure you want to delete '{0}'?",
	confirmDeleteMultipleFileTitleText: 'Confirm multiple file delete',
	confirmDeleteMultipleFileMsgText: "Are you sure you want to delete these {0} files?",	
	actionRequestFailureTitleText: 'Oh dear..',
	actionRequestFailureMsgText: "Tt seems like we're having problems. We can't send your request until we solve this. Sorry.",
	actionResponseFailureTitleText: 'PANIC!!',
	actionResponseFailureMsgText: 'Try again later please.'
};