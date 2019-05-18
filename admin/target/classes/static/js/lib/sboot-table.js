$(function(){
	$.extend({
		//有些控件无数把数据绑定到way中，如bootstarpdate icheck select，需要覆盖此方法
		getSearchCondition : function() {
			$(".search").each(function(){
				way.set("model.search."+$(this).attr("way-data"),$(this).val());
			});
			return way.get("model.search");
		}
	});
	//Bootstrap-table 列配置
	$.extend($.fn.bootstrapTable.columnDefaults,{
		align: 'center',
        valign: 'middle',
    });
	// 分页表格 默认值设置
	$.extend($.fn.bootstrapTable.defaults, {
		
	});
});

/*判断终端是手机还是电脑--用于判断文件是否导出(电脑需要导出)*/
function phoneOrPc(){
	var sUserAgent = navigator.userAgent.toLowerCase();  
	var bIsIpad = sUserAgent.match(/ipad/i) == "ipad";  
	var bIsIphoneOs = sUserAgent.match(/iphone os/i) == "iphone os";  
	var bIsMidp = sUserAgent.match(/midp/i) == "midp";  
	var bIsUc7 = sUserAgent.match(/rv:1.2.3.4/i) == "rv:1.2.3.4";  
	var bIsUc = sUserAgent.match(/ucweb/i) == "ucweb";  
	var bIsAndroid = sUserAgent.match(/android/i) == "android";  
	var bIsCE = sUserAgent.match(/windows ce/i) == "windows ce";  
	var bIsWM = sUserAgent.match(/windows mobile/i) == "windows mobile";  
	if (bIsIpad || bIsIphoneOs || bIsMidp || bIsUc7 || bIsUc || bIsAndroid || bIsCE || bIsWM) {  
		return false;  
	} else {  
	    return true; 
	}  
}

//添加事件
var Table = {
	list: {},//表格数据
	defaults:{
		url: '',
		method : 'post', //请求方式（*）
		toolbar: ".toolbar", //工具栏
		striped : true, //是否显示行间隔色
		search: true, //是否启用快速搜索
		cache: false,   //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
		pagination: true, //是否显示分页（*）
		sortable: false,  //是否启用排序
		sortOrder: "asc", //排序方式
		//sortName:'update_date', 默认顺序
		showExport: phoneOrPc(),//是否显示导出按钮
        exportDataType: "all",//导出表格方式（默认basic：只导出当前页的表格数据；all：导出所有数据；selected：导出选中的数据）
        exportTypes: ['json', 'xml', 'csv', 'txt', 'doc', 'excel'],//导出文件类型 ，支持多种类型文件导出
        //exportOptions: {},  //导出的表格参数设置
        pageNumber: 1,//初始化加载第一页，默认第一页
		pageSize : 10,//每页的记录行数（*）
		pageList : [ 20, 50, 100, 'All'],//可供选择的每页的行数（*）
		paginationLoop : false, //设置为true以启用分页连续循环模式
		sidePagination : 'server',//分页方式：client客户端分页，server服务端分页（*）
		idField : 'id',//指定主键列
		uniqueId : 'id',//为每一行指定唯一的标识符 一般为主键列
		clickToSelect : true,//是否启用点击选中行
		dblClickToEdit: true, //是否启用双击编辑
		singleSelect : false,//是否启用单选
		minimumCountColumns: 2,//最少允许的列数
		contentType : 'application/x-www-form-urlencoded',
		locale: 'zh-CN',
		showToggle: true,     //是否显示详细视图和列表视图的切换按钮
		showColumns: true,    //是否显示所有的列
		showRefresh: true,   //是否显示刷新按钮
		strictSearch: true, //Enable the strict search. 默认false
		cardView: false,  //是否显示卡片视图  用于适配移动视图
		detailView: false, //是否显示父子表
		escape: true, //是否对内容进行转义
		queryParams : function(params) {//传递参数
			var param = {
				pageNumber : this.pageNumber,//页码
				pageSize : this.pageSize,//页面大小
				sortOrder: this.sortOrder,
	            sortName : this.sortName,
			}
			//将查询表单内容赋值
			var data = $.getSearchCondition();
			$.extend(param, data);
			return param;
		},
		onDblClickRow:function(row, $element, field){
//			$('#table').bootstrapTable('checkBy', {field:'id',values:[row.id]});
//			$("#edit:visible").click();
		},
		responseHandler : function(res) {
			if (res) {
				//根据后台返回数据进行修改
				return res;
			} else {
				return {};
			}
		},
		extend: {
            index_url: '',
            add_url: '',
            edit_url: '',
            del_url: '',
            import_url: '',
            multi_url: '',
            dragsort_url: '',
		}
	},
	config: {
        firsttd: 'tbody tr td:first-child:not(:has(div.card-views))',
        toolbar: '.toolbar',
        refreshbtn: '.btn-refresh',
        addbtn: '.btn-add',
        editbtn: '.btn-edit',
        delbtn: '.btn-del',
        importbtn: '.btn-import',
        multibtn: '.btn-multi',
        disabledbtn: '.btn-disabled',
        editonebtn: '.btn-editone',
        dragsortfield: 'sort',
    },
	//初始化
	init : function(defaults){
		defaults = defaults ? defaults : {};
		// 如果是iOS设备则启用卡片视图
        if (navigator.userAgent.match(/(iPod|iPhone|iPad)/)) {
            Table.defaults.cardView = true;
        }
        // 写入bootstrap-table默认配置
        $.extend(true, $.fn.bootstrapTable.defaults, Table.defaults, defaults);
	},
	// 绑定事件
    bindevent: function (table) {
    	//Bootstrap-table的父元素,包含table,toolbar,pagnation
        var parenttable = table.closest('.bootstrap-table');
        //Bootstrap-table配置
        var options = table.bootstrapTable('getOptions');
        //Bootstrap操作区
        var toolbar = $(options.toolbar, parenttable);
        //当刷新表格时
        table.on('refresh.bs.table', function (e, settings, data) {
            $(Table.config.refreshbtn, toolbar).find(".fa").addClass("fa-spin");
        });
        // 刷新按钮事件
        $(toolbar).on('click', Table.config.refreshbtn, function () {
            table.bootstrapTable('refresh');
        });
        if (options.dblClickToEdit) {
            //当双击单元格时
            table.on('dbl-click-row.bs.table', function (e, row, element, field) {
                $(Table.config.editonebtn, element).trigger("click");
            });
        }
        // 导入按钮事件
        if ($(Table.config.importbtn, toolbar).size() > 0) {
//            require(['upload'], function (Upload) {
//                Upload.api.plupload($(Table.config.importbtn, toolbar), function (data, ret) {
//                    Fast.api.ajax({
//                        url: options.extend.import_url,
//                        data: {file: data.url},
//                    }, function (data, ret) {
//                        table.bootstrapTable('refresh');
//                    });
//                });
//            });
        }
        
        // 添加按钮事件
        $(toolbar).on('click', Table.config.addbtn, function () {
            var ids = Table.selectedids(table);
            var url = options.extend.add_url;
            if (url.indexOf("{ids}") !== -1) {
                url = Table.replaceurl(url, {ids: ids.length > 0 ? ids.join(",") : 0}, table);
            }
            //Fast.api.open(url, __('Add'), $(this).data() || {});
        });
        // 批量编辑按钮事件
        $(toolbar).on('click', Table.config.editbtn, function () {
            var that = this;
            //循环弹出多个编辑框
            $.each(table.bootstrapTable('getSelections'), function (index, row) {
                var url = options.extend.edit_url;
                row = $.extend({}, row ? row : {}, {ids: row[options.pk]});
                var url = Table.replaceurl(url, row, table);
                //Fast.api.open(url, __('Edit'), $(that).data() || {});
            });
        });
       // 批量删除按钮事件
        $(toolbar).on('click', Table.config.delbtn, function () {
            var that = this;
            var ids = Table.selectedids(table);
//            Layer.confirm(
//                __('Are you sure you want to delete the %s selected item?', ids.length),
//                {icon: 3, title: __('Warning'), offset: 0, shadeClose: true},
//                function (index) {
//                    Table.api.multi("del", ids, table, that);
//                    Layer.close(index);
//                }
//            );
        });
        $(table).on("click", "input[data-id][name='checkbox']", function (e) {
            var ids = $(this).data("id");
            var row = Table.getrowbyid(table, ids);
            table.trigger('check.bs.table', [row, this]);
        });
        //表格中的编辑操作
        $(table).on("click", "[data-id].btn-edit", function (e) {
            e.preventDefault();
            var ids = $(this).data("id");
            var row = Table.getrowbyid(table, ids);
            row.ids = ids;
            var url = Table.replaceurl(options.extend.edit_url, row, table);
            //Fast.api.open(url, __('Edit'), $(this).data() || {});
        });
        //表格中的删除操作
        $(table).on("click", "[data-id].btn-del", function (e) {
            e.preventDefault();
            var id = $(this).data("id");
            layer.confirm('确定删除？', 
                {icon: 3, anim : 6, btn : [ '确定', '取消' ], shadeClose: true},
                function (index) {
                    $.post(options.extend.del_url, {
    					id : id
    				}, function(respone) {
    					if (respone.responeCode === 200) {
    						layer.msg("删除成功");
    						table.bootstrapTable('refresh');
    					} 
    				});
                    layer.close(index);
                }
            );
        });
    },
    // 单元格元素事件
    events: {
        operate: {
            'click .btn-editone': function (e, value, row, index) {
                e.stopPropagation();
                e.preventDefault();
                var table = $(this).closest('table');
                var options = table.bootstrapTable('getOptions');
                var ids = row[options.pk];
                row = $.extend({}, row ? row : {}, {ids: ids});
                var url = options.extend.edit_url;
               // Fast.api.open(Table.api.replaceurl(url, row, table), __('Edit'), $(this).data() || {});
            },
            'click .btn-delone': function (e, value, row, index) {
                e.stopPropagation();
                e.preventDefault();
                var that = this;
                var top = $(that).offset().top - $(window).scrollTop();
                var left = $(that).offset().left - $(window).scrollLeft() - 260;
                if (top + 154 > $(window).height()) {
                    top = top - 154;
                }
                if ($(window).width() < 480) {
                    top = left = undefined;
                }
//                Layer.confirm(
//                    __('Are you sure you want to delete this item?'),
//                    {icon: 3, title: __('Warning'), offset: [top, left], shadeClose: true},
//                    function (index) {
//                        var table = $(that).closest('table');
//                        var options = table.bootstrapTable('getOptions');
//                        Table.api.multi("del", row[options.pk], table, that);
//                        Layer.close(index);
//                    }
//                );
            }
        }
    },
    // 单元格数据格式化
    formatter: {
    	icon: function (value, row, index) {
            if (!value)
                return '';
            value = value === null ? '' : value.toString();
            value = value.indexOf(" ") > -1 ? value : "fa fa-" + value;
            //渲染fontawesome图标
            return '<i class="' + value + '"></i> ' + value;
        },
    	datetime: function (value, row, index) {
            var datetimeFormat = typeof this.datetimeFormat === 'undefined' ? 'yyyy-MM-dd hh:mm:ss' : this.datetimeFormat;
            if (isNaN(value)) {
                return value ? new Date(value).format(datetimeFormat) : '';
            } else {
                return value ? new Date(parseInt(value) * 1000).format(datetimeFormat) : '';
            }
        },
        operate: function (value, row, index) {
            var table = this.table;
            // 操作配置
            var options = table ? table.bootstrapTable('getOptions') : {};
            // 默认按钮组
            var buttons = $.extend([], this.buttons || []);
            // 所有按钮名称
            var names = [];
            buttons.forEach(function (item) {
                names.push(item.name);
            });
            if (options.extend.dragsort_url !== '' && names.indexOf('dragsort') === -1) {
                buttons.push({
                    name: 'dragsort',
                    icon: 'fa fa-arrows',
                    title: '拖动排序',
                    extend: 'data-toggle="tooltip"',
                    classname: 'btn btn-xs btn-primary btn-dragsort'
                });
            }
            if (options.extend.edit_url !== '' && names.indexOf('edit') === -1) {
                buttons.push({
                    name: 'edit',
                    icon: 'fa fa-pencil',
                    title: '编辑',
                    extend: 'data-toggle="tooltip"',
                    classname: 'btn btn-xs btn-success btn-editone',
                    url: options.extend.edit_url
                });
            }
            if (options.extend.del_url !== '' && names.indexOf('del') === -1) {
                buttons.push({
                    name: 'del',
                    icon: 'fa fa-trash',
                    title: '删除',
                    extend: 'data-toggle="tooltip"',
                    classname: 'btn btn-xs btn-danger btn-delone'
                });
            }
            return Table.buttonlink(this, buttons, value, row, index, 'operate');
        }
    },
    buttonlink: function (column, buttons, value, row, index, type) {
        var table = column.table;
        type = typeof type === 'undefined' ? 'buttons' : type;
        var options = table ? table.bootstrapTable('getOptions') : {};
        var html = [];
        var hidden, visible, url, classname, icon, text, title, refresh, confirm, extend;
        var fieldIndex = column.fieldIndex;
        $.each(buttons, function (i, j) {
            if (type === 'operate') {
                if (j.name === 'dragsort' && typeof row[Table.config.dragsortfield] === 'undefined') {
                    return true;
                }
                if (['add', 'edit', 'del', 'multi', 'dragsort'].indexOf(j.name) > -1 && !options.extend[j.name + "_url"]) {
                    return true;
                }
            }
            var attr = table.data(type + "-" + j.name);
            if (typeof attr === 'undefined' || attr) {
                hidden = typeof j.hidden === 'function' ? j.hidden.call(table, row, j) : (j.hidden ? j.hidden : false);
                if (hidden) {
                    return true;
                }
                visible = typeof j.visible === 'function' ? j.visible.call(table, row, j) : (j.visible ? j.visible : true);
                if (!visible) {
                    return true;
                }
                url = j.url ? j.url : '';
                url = typeof url === 'function' ? url.call(table, row, j) : (url ? Table.replaceurl(url, row, table) : 'javascript:;');
                classname = j.classname ? j.classname : 'btn-primary btn-' + name + 'one';
                icon = j.icon ? j.icon : '';
                text = j.text ? j.text : '';
                title = j.title ? j.title : text;
                refresh = j.refresh ? 'data-refresh="' + j.refresh + '"' : '';
                confirm = j.confirm ? 'data-confirm="' + j.confirm + '"' : '';
                extend = j.extend ? j.extend : '';
                html.push('<a href="' + url + '" class="' + classname + '" ' + (confirm ? confirm + ' ' : '') + (refresh ? refresh + ' ' : '') + extend + ' title="' + title + '" data-table-id="' + (table ? table.attr("id") : '') + '" data-field-index="' + fieldIndex + '" data-row-index="' + index + '" data-button-index="' + i + '"><i class="' + icon + '"></i>' + (text ? ' ' + text : '') + '</a>');
            }
        });
        return html.join(' ');
    },
    //替换URL中的数据
    replaceurl: function (url, row, table) {
        var options = table ? table.bootstrapTable('getOptions') : null;
        var ids = options ? row[options.uniqueId] : 0;
        row.ids = ids ? ids : (typeof row.ids !== 'undefined' ? row.ids : 0);
        //自动添加ids参数
        url = !url.match(/\{ids\}/i) ? url + (url.match(/(\?|&)+/) ? "&ids=" : "/ids/") + '{ids}' : url;
        url = url.replace(/\{(.*?)\}/gi, function (matched) {
            matched = matched.substring(1, matched.length - 1);
            if (matched.indexOf(".") !== -1) {
                var temp = row;
                var arr = matched.split(/\./);
                for (var i = 0; i < arr.length; i++) {
                    if (typeof temp[arr[i]] !== 'undefined') {
                        temp = temp[arr[i]];
                    }
                }
                return typeof temp === 'object' ? '' : temp;
            }
            return row[matched];
        });
        return url;
    },
    // 获取选中的条目ID集合
    selectedids: function (table) {
        var options = table.bootstrapTable('getOptions');
        return $.map(table.bootstrapTable('getSelections'), function (row) {
            return row[options.uniqueId];
        });
    },
    // 根据行索引获取行数据
    getrowdata: function (table, index) {
        index = parseInt(index);
        var data = table.bootstrapTable('getData');
        return typeof data[index] !== 'undefined' ? data[index] : null;
    },
    // 根据主键ID获取行数据
    getrowbyid: function (table, id) {
        var row = {};
        var options = table.bootstrapTable("getOptions");
        $.each(table.bootstrapTable('getData'), function (i, j) {
            if (j[options.uniqueId] == id) {
                row = j;
                return false;
            }
        });
        return row;
    }
} 

/**
 * bootstart table bug 修改
 */
!function($) {
    'use strict';

    var BootstrapTable = $.fn.bootstrapTable.Constructor;
    BootstrapTable.prototype.onSort = function (event) {
        var $this = event.type === "keypress" ? $(event.currentTarget) : $(event.currentTarget).parent(),
            $this_ = this.$header.find('th').eq($this.index());

        this.$header.add(this.$header_).find('span.order').remove();

        if (this.options.sortName === $this.data('field')) {
            this.options.sortOrder = this.options.sortOrder === 'asc' ? 'desc' : 'asc';
        } else {
            this.options.sortName = $this.data('sort-name') ? $this.data('sort-name') : $this.data('field');
            this.options.sortOrder = $this.data('order') === 'asc' ? 'desc' : 'asc';
        }
        this.trigger('sort', this.options.sortName, this.options.sortOrder);

        $this.add($this_).data('order', this.options.sortOrder);

        // Assign the correct sortable arrow
        this.getCaret();

        if (this.options.sidePagination === 'server') {
            this.initServer(this.options.silentSort);
            return;
        }

        this.initSort();
        this.initBody();
    };
    BootstrapTable.prototype.getCaret = function () {
        var that = this;
        $.each(this.$header.find('th'), function (i, th) {
            var sort_class = $(th).data('field') === that.options.sortName || $(th).data('sort-name') === that.options.sortName ? that.options.sortOrder : 'both';
            $(th).find('.sortable').removeClass('desc asc').addClass(sort_class);
        });
    };
}(jQuery);