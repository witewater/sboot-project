$(function() {
	model = {
		path : adminContextPath,
		//递归菜单
		handLeftMenu:function(allMenus,parentId){
			var menuHtml = "";
			var active = "";
			$.each(allMenus,function(i,v){//
				if (v.pid == parentId) {//先处理第一级的
					 if (v.type == '1') {//目录
						 menuHtml +="<li class='treeview " + active+ "' data-level='0'>" +
						 "<a href='#' class='nav-link'>" +
				            "<i class='"+ v.icon +"'></i><span>" + v.name+ "</span>"+
				            "<span class='pull-right-container'>"+
				             " <i class='fa fa-angle-left pull-right'></i>"+
				           " </span>"+
				          "</a>" +
				          "<ul class='treeview-menu'>" +
				          model.handLeftMenu(allMenus,v.id) + 
				          "</ul>" +
				          "</li>";
					 } else if (v.type == '2') {//菜单
						 /*if (!v.target) {
							 v.target = 'main';
						 }
						 if (!v.url) {
							 v.url = 'javascript:void(0)';
						 }*/
						 menuHtml += "<li class='" + active + "' data-level='1'>" +
						    /*"<a href='" + v.href + "' target='" + v.target  + "'>" +*/
				            "<a class='nav-link' href='javascript:void(0)' data-title='" + v.name + "' data-url='" + v.url + "' data-id='" + v.id +
				            "' onclick='myAddTabs(this)'><i class='" + v.icon + "'></i><span>" + v.name+ "</span>" +
				          "</a>" +
				        "</li>";
					 }
				}
			});
			return menuHtml;
		},
		getUserInfo:function(callback){
			//个人中心
			$.post(model.path + "/getUserInfo",function(respone){
				var data = respone.data;
				callback(data);
		    });
		},
		resetForm:function() {
			//false 表示不清除数据
			$("#data-form,#passoword-form").bootstrapValidator('resetForm', false);
			$("#passoword-form")[0].reset();
		},
		//设置个人中心数据
		setFormData:function(data){
			way.set("model.form.data",data);
		},
		getFormData : function() {
			var data =  way.get("model.form.data");
			return data?data:{};
		},
		userCenterLayerIndex:0,
		//按钮权限初始化
		bntPermissionData:function(data){
			sessionStorage.clear();
			if (data) {
				$.each(data,function(i,v){
					if (v.permission) {
						var pers = $.trim(v.permission).split(",");
						$.each(pers,function(i,v){
							if (v) {
								sessionStorage.setItem("model.permission." + v,"1");
							}
						});
					}
				});
			}
		},
		init:function(){
			//iframe的高度100%  父容器必须是实际高度
			$("#content-wrapper").height($("#content-wrapper").height()-150);
			//菜单渲染
			$.post(model.path + "/getUserMenus",function(respone){
				var menu = "";
				var data = respone.data;
				//按钮权限
				model.bntPermissionData(data);
				//$('.sidebar-menu').sidebarMenu({data: data});
				//处理菜单
				$("#left-menu").append(model.handLeftMenu(data,'0'));
				$("#left-menu li:eq(0)").addClass("active");
		    });
			//右上角
			model.getUserInfo(function(data){
				if (data) {
					if (data.pictureFullUrl) {
						$(".user-photo").attr("src",data.pictureFullUrl);
					}
					$("#userName").text(data.nickname);
					$("#loginName").text(data.username + "[" + data.nickname + "]");
				}
			});
		},
	}
	model.init();
//	/**
//	 * 左侧菜单点击选中效果
//	 */
//	$("body").on("click","#left-menu > li", function() {
//		var $this = $(this);
//		//$this.addClass("active");
//		//$this.siblings().removeClass("active");
//	});
	//最大化窗口 
    $("#fullScreen").on("click", function (e) {
        e.preventDefault();
        if (!$(this).hasClass("full-on")) {
            var docElm = document.documentElement;
            var full = docElm.requestFullScreen || docElm.webkitRequestFullScreen ||
                docElm.mozRequestFullScreen || docElm.msRequestFullscreen;
            "undefined" !== typeof full && full && full.call(docElm);
        } else {
            document.exitFullscreen ? document.exitFullscreen()
                : document.mozCancelFullScreen ? document.mozCancelFullScreen()
                : document.webkitCancelFullScreen ? document.webkitCancelFullScreen()
                : document.msExitFullscreen && document.msExitFullscreen()
        }
        $(this).toggleClass("full-on");
    });
	//退出
	$("#login-out").click(function(){
		 $.post(model.path + "/logout",function(respone){
				window.location.href="/login";
		});
	});
	//个人中心
	$("#user-center").click(function(){
		model.resetForm();
		$('#user-center-tabs a[href="#tab_info"]').tab('show');
		model.getUserInfo(function(data){
			console.log(data);
			model.setFormData(data);
		});
		//个人中心
		model.userCenterLayerIndex = layer.open({
		 	  title:'个人中心',
              shadeClose:true,
		 	  icon: 1,
		 	  offset: '100px',
		 	  area: ['640px'],
			  type: 1, 
			  content:  $("#user-center-wrapper") 
			});
	});
	// 校验
	$("#data-form").bootstrapValidator({  
		excluded:[":disabled"],   //只对禁用域不进行验证
	}).on("success.form.bv", function(e) {// 提交
		e.preventDefault();// 阻止默认事件提交
		$.post(model.path + "/user_info", $("#data-form").serialize(), function(respone) {
			if (respone.code === 200) {
				var pictureFullUrl = model.getFormData().pictureFullUrl;
				//如果图像存在
				if (pictureFullUrl) {
					$(".user-photo").attr("src",pictureFullUrl);
				};
				$("#userName").text(model.getFormData().nickname);
				layer.msg("保存成功");
				layer.close(model.userCenterLayerIndex);
			}else{
				layer.msg(respone.msg);
			}
		});
	});
	// 密码校验
	$("#passoword-form").bootstrapValidator({
		fields:{
			password:{
				validators:{
					notEmpty:{message:'请填写新密码'},
					identical:{//相同
						message:'两次密码不一致',
						field:'conFirmPassword'
					}
				}
			},
			conFirmPassword:{
				validators:{
					notEmpty:{message:'请填写确认密码'},
					identical:{//相同
						message:'两次密码不一致',
						field:'password'
					}
				}
			},
			oldPassword:{
				threshold:5,//有5字符以上才发送ajax请求
				validators:{
					notEmpty:{message:'请填写原密码'},
					remote: {
	                    url: model.path + "/checkPassword",//验证原密码
	                    message: '原密码错误',//提示消息
	                    type:'post',
	                    delay: 500,//每输入一个字符，就发ajax请求，服务器压力还是太大，设置2秒发送一次ajax（默认输入一个字符，提交一次，服务器压力太大）
	                },
				}
			}
		}
	}).on("success.form.bv", function(e) {// 提交
		e.preventDefault();
		$.post(model.path + "/edit_pwd", $("#passoword-form").serialize(), function(respone) {
			if (respone.code === 200) {
				layer.msg("保存成功");
				layer.close(model.userCenterLayerIndex);
			}else{
				layer.msg(respone.msg);
			}
		});
	});
	//图像上传
	$('#userImageUpload').fileupload({
		 url:adminContextPath + "/file/uploadImage",
		 type:'POST',
		 formData:null,
		 change: function (e, data) {
			 var file = data.files[0];
		    if (file.size > 2 * 1024 * 1024) {
		    		layer.msg(file.name + " 文件大小超过2M,请重新选择");
		    		return false;
		    }
		  // 开头为image/
			var reg = /^image\//
		    if (!reg.test(file.type)) {
			    	layer.msg(file.name + " 不是图片格式");
		    		return false;
		    }
		  },
		 done: function (e, response) {//设置文件上传完毕事件的回调函数 
			 if (response.result.responeCode == '0') {
				// layer.msg(response.result.data.originalFilename + " 上传成功");
				 way.set("model.form.data.photo",response.result.data.relativePath);
				 way.set("model.form.data.photoFullUrl",response.result.data.fullUrl);
			 } 
        }, 
	 });
	
});

//添加菜单导航
function myAddTabs(dom){
	var title=$(dom).data("title");
    var url=$(dom).data("url");
    var id=$(dom).data("id");
    addTabs({id:id,title:title,close: true,url:url,urlType:'relative'});
}

/*window.isMenuClickFlag = false;
$(window).bind("hashchange", function(g) {
    if (!window.isMenuClickFlag) {
        var f = window.location.hash.replace("#", "");
        if (f && f != "" && f != window.location.pathname) {
            var d = $('a[data-url="' + f + '"]:eq(0)');
            if (d && d.length > 0) {
                d.click()
            } else {
                 
            }
        } else {
            $(".sidebar-menu > li:eq(0):not(.active) > a:eq(0)").click()
        }
    }
    window.isMenuClickFlag = false
}).trigger("hashchange");
*/

//$.fn.extend({
//    animateCss: function(animationName, callback) {
//        var animationEnd = (function(el) {
//            var animations = {
//                animation: 'animationend',
//                OAnimation: 'oAnimationEnd',
//                MozAnimation: 'mozAnimationEnd',
//                WebkitAnimation: 'webkitAnimationEnd'
//            };
//            for (var t in animations) {
//                if (el.style[t] !== undefined) {
//                    return animations[t];
//                }
//            }
//        })(document.createElement('div'));
//        this.addClass('animated ' + animationName).one(animationEnd, function() {
//            $(this).removeClass('animated ' + animationName);
//            if (typeof callback === 'function') callback();
//        });
//        return this;
//    }
//});

/**
 * 适配移动端并初始化菜单
 */
/*$(document).ready(function () {
    if($(window).width()<1024){
        if($('body').hasClass('layout-boxed')){
            $('body').removeClass('layout-boxed');
        }
        if($('body').hasClass('sidebar-collapse')){
            $('body').removeClass('sidebar-collapse');
        }
    }
    initMenu();
    $("#animated-header,#animated-content").animateCss("fadeIn");
});*/

/*function initMenu() {
    var pathName = location.pathname;
    $(".sidebar-menu").children().each(function () {
        var li = $(this);
        li.find('a').each(function () {
            var href = $(this).attr("href");
            if (pathName == href) {
                li.addClass("active");
                $(this).parent().addClass("active");
            }else{
                $(this).parent().removeClass("active");
            }
        });
    });
}*/


//$(function(){
//	//收缩左侧菜单
//    $(".sidebar-toggle").click(function(){
//        var obj = $(this);
//        if ($(".main-sidebar").css("width") == "50px") {
//            //改变右侧部分宽度
//            $('.easyui-tabs').tabs("resize",{
//                width:$('.easyui-tabs').parent().width()
//            });
//        } else {
//            //恢复右侧部分宽度
//            $('.easyui-tabs').tabs("resize",{
//                width:$('.easyui-tabs').parent().width(),
//                fit:true,
//                scrollDuration:1000
//            });
//        }
//    });
//	
//	//用户在切换选项卡时，和导航树保持同步
//    $('.easyui-tabs').tabs({
//        onSelect: function(title){
//            var _select = $(".easyui-tabs").tabs("getSelected");
//            var obj = _select.panel("options").tab;
//            //循环左侧菜单里每个菜单的text进行选项卡的title进行对比
//            $("#left-menu").find("li").each(function(){
//                var target = $(this);
//                if(target.text() == obj.text()){
//                    //左侧对应菜单展开选中
//                    target.addClass("active");
//                    target.siblings().removeClass("active");
//                }
//            })
//        }
//    });
//    
//    //当关闭最后一个选项卡时，隐藏选项卡页面显示主界面
//    $('.easyui-tabs').tabs({
//        onClose:function(){
//            if($('.easyui-tabs').tabs('tabs').length==0){
//            	$(".wellcome").show();
//                $(".easyui-tabs").hide();
//                $(".treeview-menu a").removeClass("active");
//                
//            }
//        }
//    });
//});

////浏览器窗口调整事件
//$(window).resize(function () {  
//	 $('.easyui-tabs').tabs("resize",{
//	    width: $('.easyui-tabs').parent().width(),//选项卡容器宽度
//	    fit:true,//选项卡大小铺满容器
//	    scrollDuration:1000//每次滚动动画持续时间，默认400ms
//	 });  
// });