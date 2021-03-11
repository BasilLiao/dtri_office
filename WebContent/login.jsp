<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login DTR ERP Expansion system(延展系統)</title>
</head>
<link rel="stylesheet" href="./thirdparty/css/bootstrap-4.4.1.min.css">
<script src="./thirdparty/js/jquery-3.4.1.min.js"></script>
<script src="./thirdparty/js/jquery-ui-1.12.1.min.js"></script>
<script src="./thirdparty/js/bootstrap-4.4.1.min.js"></script>
<link rel="icon" type="image/svg" href="./emoji.svg">
<style type="text/css">
body {
	background-image: url("./thirdparty/images/bg.png");
	font-family : Microsoft JhengHei;
}
</style>
</head>

<body id="login" class="container">
	<div id="header" class="mt-5 mb-3">
		<div class="text-center">
			<h3>DTR ERP</h3>
		</div>
	</div>
	<div id="nav" class="mt-3 mb-3">
		<div class="text-center">導覽</div>
	</div>
	<div id="body" class="mt-3 mb-3">
		<form action="./index.do"  data-toggle="validator" method="post">
			<div class="row">
				<div class="col-md-4"></div>
				<div class="col-md-4 mt-2 mb-3 p-4 border border-primary rounded bg-white shadow-lg">
					<div class="row m-0 mb-3">
						<h3>Please sign in</h3>
					</div>

					<div class="row m-0 mb-3">
						<div class="col-md-4 ">Account</div>
						<input class="col-md-8" type="text" id="account" name="inputAccount"
							placeholder="Account" required autofocus>
					</div>

					<div class="row m-0 mb-3">
						<div class="col-md-4">Password</div>
						<input class="col-md-8" type="password" id="password"
							name="inputPassword" placeholder="Password" required>
					</div>

					<div class="row m-0 mb-3">
						<div class="col-md-10">
							<input type="checkbox" value="remember-me"> Remember me
						</div>
					</div>

					<div class="mb-3 m-0 text-center">
						<button class="col-md-12 m-0 btn btn-primary"
							type="submit">Sign in</button>
					</div>
				</div>
				<div class="col-md-4"></div>
			</div>
		</form>
	</div>
	<div id="footer" class="mt-5 mb-3">
		<div class="text-muted text-center">
			<p>請使用 1.Brave 2.Chrome 3.Microsoft Edge 瀏覽器
			<br>最佳解析度為 1366*768 以上.
			<br>任何問題 需求 請洽詢 系統管理員 分機:321 </p>
			<p>&copy; 2020 DT Research, Inc. All Rights Reserved.<br>
			版本: Beta v0.74.2</p>
		</div>
	</div>
<!-- 必要訊息${allData}--> 
</body>
<script>
	//控制端
 
</script>
</html>