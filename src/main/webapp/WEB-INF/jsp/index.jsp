<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>网站微信扫码支付</title>
</head>
<body>
	<form action="<%=request.getContextPath() %>/createPreOrder" method="post">
		<table>
			<tr>
				<td>支付金额（单位：分）：</td>
				<td><input type="text" name="amount" /></td>
			</tr>
			<tr>
				<td>付款人：</td>
				<td><input type="text" name="title" /></td>
			</tr>
			<tr>
				<td></td>
				<td><input type="submit" value="提交" /></td>
			</tr>
		</table>
	</form>
</body>
</html>