$(function() {
	$(document).keydown(function(event) {
		if (event.keyCode == 13) {
			$("#login").click();
		}
	});
	$("#login").click(function() {
		var loginName = $.trim($("input[name='username']").val());
		var password = $.trim($("input[name='password']").val());
		var rememberMe = $.trim($("input[name='rememberMe']").val());
		var captcha = $.trim($("input[name='captcha']").val());
		if (!loginName) {
			layer.msg("用户名不能为空", {
				offset : 'm',
				anim : 6
			});
			return false;
		}
		if (loginName.length < 2) {
			layer.msg("用户名过短", {
				offset : 'm',
				anim : 6
			});
			return false;
		}
		if (!password) {
			layer.msg("密码不能为空", {
				offset : 'm',
				anim : 6
			});
			return false;
		}
		if (password.length < 6) {
			layer.msg("密码不能少于6位", {
				offset : 'm',
				anim : 6
			});
			return false;
		}
		if($("#isCaptcha").val() === 'true'){
			if (!captcha) {
				layer.msg("验证码不能为空", {
					offset : 'm',
					anim : 6
				});
				return false;
			}
		}
		var form = $(this).parents("form");
	    var url = form.attr("action");
		$.post(url, {
			username : loginName,
			password : password,
			captcha : captcha,
			rememberMe : rememberMe
		}, function(respone) {
			if (respone.code === 200) {
				localStorage.setItem('username', $("#username").val());
				layer.msg(respone.msg, {offset : 'm', anim : 1});
				sessionStorage.clear();
				window.location.href = "/";
			}else{
				layer.msg(respone.msg, {offset : 'm', anim : 6});
				$(".captcha-img").click();
				if(respone.attrs != null){
					if(respone.attrs.isCaptcha){
						$("#isCaptchaShow").show();
						$("#isCaptcha").val(respone.attrs.isCaptcha);
					}
				}
			}
		});
	});
	$("#username").val(localStorage.getItem("username"));
});