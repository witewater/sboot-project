/**
 * 依赖layer.js
 */
//后台请求地址
var requestPath = /*[[@{/}]]*/'';
// 后端应用请求
var adminContextPath = requestPath;
//alert(requestPath);
//alert(window.location.pathname);
/* JAX的全局默认选项，处理连接超时问题 */
$.ajaxSetup({
	global : true, // 默认就是true ，触发全局事件
	type : "POST", // 默认使用POST方式
	contentType : "application/x-www-form-urlencoded;charset=utf-8",// 发送数据到服务器时所使用的内容类型
	timeout : 10000, // 设置本地的请求超时时间（以毫秒计）。
	dataType : 'json',
	complete : function(xhr, status) {// 请求完成时运行的函数（在请求成功或失败之后均调用，即在 success 和
										// error 函数之后）。
		if (xhr.status == 401) {
			layer.confirm('session连接超时，是否重新登录？', {
				btn : [ '是', '否' ]
			}, function() {
				if (window.parent.window != window) {
					window.top.location.href = window.location.pathname + '/login';
				}
			});
		}
	},
	xhrFields : {
		withCredentials : true
	},//跨域allowCredentials
	crossDomain : true,// 是否跨域
	statusCode : {
		310 : function() {// 未登录
			if (window.location.href.indexOf("/login") == -1) {
				if (self != top) {
					window.top.location.href = window.location.pathname + "/login";
				} else {
					window.location.href = window.location.pathname + "/login";
				}
			}
		},
		311 : function() {
			layer.msg("未授权，请联系管理员");
		},
		404 : function() {
			layer.msg("请求路径错误");
		},
		500 : function() {
			layer.msg("服务器故障");
		}
	}
// 同步设置
// async : false //布尔值，表示请求是否异步处理。默认是 true。
// cache : false //布尔值，表示浏览器是否缓存被请求页面。默认是 true。
});

//wayjs 默认不存储localstoreage
way.options.persistent = false;

/** AJAX 全局事件 **/
$(document).ajaxStart(function() {// 开始
	//Pace.restart();
	layer.load({
		time : 10 * 1000
	});
}).ajaxStop(function() {// 结束
	layer.closeAll('loading');
}).ajaxError(function(event, jqxhr, settings, thrownError) {// 异常
	layer.closeAll('loading');
	if ("timeout" == thrownError) {
		layer.msg("请求超时，请重试");
	} else if ("Not Found" == thrownError) {
		layer.msg("请求路径错误，请检查");
	} else {
		if (!jqxhr.status) {
			layer.msg("服务器故障");
		}
	}
}).ajaxSuccess(function(event, xhr, settings) {
	if (xhr.responseText) {
		var data = $.parseJSON(xhr.responseText);
		var responeCode = data.code;
		//0000 开头为系统错误码，此处为系统定义的错误代码，还需要修改
		var reg = /^0000/
		if (responeCode == '-1' || reg.test(responeCode)) {
			layer.msg(data.msg);
		}
	}
});
/* 消息类封装 */
$.Messager = function (result) {
    if (result.code === 200) {
        layer.msg(result.msg, {offset: '15px', time: 3000, icon: 1});
        setTimeout(function () {
            if (result.data == null) {
                window.location.reload();
            } else {
                window.location.href = result.data
            }
        }, 2000);
    } else {
        layer.msg(result.msg, {offset: '15px', time: 3000, icon: 2});
    }
};
/* post提交表单数据 */
$(document).on("click", ".ajax-post", function (e) {
    e.preventDefault();
    var form = $(this).parents("form");
    var url = form.attr("action");
    var serializeArray = form.serializeArray();
    $.post(url, serializeArray, function (result) {
        $.Messager(result);
    });
});
/* get方式异步 */
$(document).on("click", ".ajax-get", function (e) {
    e.preventDefault();
    var msg = $(this).data("msg");
    var url = $(this).href;
    if (msg !== undefined) {
        layer.confirm(msg + '？', {
            title: '提示',
            btn: ['确认', '取消']
        }, function () {
            $.get(url, function (result) {
                $.Messager(result);
            });
        });
    } else {
        $.get(url, function (result) {
            $.Messager(result);
        });
    }
});
/**
 * 保存设置选项
 */
function saveOptions(option) {
	var param = $('#'+option).serialize();
	$.ajax({
	    type: 'post',
	    url: '/admin/option/save',
	    data: param,
	    success: function (data) {
	    	$.Messager(data);
	    }
	});
}
/**
 * 提交表单 添加、删除 form表单的提交
 * 
 * @param {Object}
 *            commitUrl 表单提交地址
 * @param {Object}
 *            listUrl 表单提交成功后转向的列表页地址
 */
var MyForm = {
	addModel : function(href) {
		location.href = href;
	},
	deleteModel : function(action, callback) {
		return layer.confirm('确认删除么？', {
			btn : [ '是', '否' ]
		}, function() {
			$.ajax({
				type : "GET",
				url : action,
				dataType : "json",
				success : function(resultdata) {
					if (resultdata.code === 200) {
						layer.msg(resultdata.msg);
						if (callback) {
							callback();
						} else {
							document.location.reload();
						}
					} else {
						layer.msg(resultdata.msg);
					}
				},
				error : function(data, errorMsg) {
					layer.msg(errorMsg);
				}
			});
		});
	},
	commit : function(formId, commitUrl, jumpUrl) {
		var data = $("#" + formId).serialize();
		$.ajax({
			type : "POST",
			url : commitUrl,
			data : data,
			dataType : "json",
			success : function(resultdata) {
				if (resultdata.code === 200) {
					layer.msg(resultdata.msg);
					window.location.href = jumpUrl;
				} else {
					layer.msg(resultdata.msg);
				}
			},
			error : function(data, errorMsg) {
				layer.msg(errorMsg);
			}
		});
	}
}
/**
 * 设置select选中的值
 * @param sId
 * @param value
 * @returns
 */
function setSelectValue(sId,value){  
    var s = document.getElementById(sId);  
    var ops = s.options;  
    for(var i=0;i<ops.length; i++){  
        var tempValue = ops[i].value;  
        if(tempValue == value) {  
            ops[i].selected = true;  
        }  
    }
} 
/**
 * 回显各种控件的值
 * @param target 值
 * @param type 控件类型
 * @param name 控件名称
 */
function matchBox(target, type, name) {
    if (target.length > 0) {
        var roleArr = target.split(',');
        $.each(roleArr, function(index, roleName) {
            $('input[type=' + type + '][name=' + name + ']').each(function() {
                if ($(this).val() == roleName) {
                    $(this).prop("checked", true);
                }
            });
        });
    }
}