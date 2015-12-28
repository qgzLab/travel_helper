var uname;
var upassword;
var upassword1;
var uemail;
var uphone;
var uqq;
var img1;
var img2;
var img3;
var img4;
var img5;
var img6;
var img7;
function trim(str){
	str= str.replace(/^\s+/g,""); //去前空格
	str= str.replace(/\s+$/g,""); //去后空格
	return str;
}

function init(){
	uname = document.getElementById("uname");
	upassword = document.getElementById("upassword");
	upassword1 = document.getElementById("upassword1");
	uemail = document.getElementById("uemail");
	uphone = document.getElementById("uphone");
	uqq = document.getElementById("uqq");
	img1 = document.getElementById("img1");
	img2 = document.getElementById("img2");
	img3 = document.getElementById("img3");
	img4 = document.getElementById("img4");
	img5 = document.getElementById("img5");
	img6 = document.getElementById("img6");
	img7 = document.getElementById("img7");
}

function checkName(imgobj,textobj){
	imgobj.style.visibility = "visible";
	textobj.value = trim(textobj.value); 
	if(textobj.value.length<2){
		textobj.style.backgroundColor = "#AAAAAA";
		imgobj.src = "/static/images/err.png";
		return false;
	}

	textobj.style.backgroundColor = "#fff";
	imgobj.src = "/static/images/ok.png";
	return true;
}

function getfocus(textobj,imgobj){
	imgobj.style.visibility = "hidden";//设置相应的图像对象隐藏
	textobj.style.backgroundColor = "#fff";//设置相应的输入框背景色为白色
}

function checkPass(imgobj,textobj){
	imgobj.style.visibility = "visible";
	textobj.value = trim(textobj.value);
	if(textobj.value.length<6){
		textobj.style.backgroundColor = "#AAAAAA";
		imgobj.src = "/static/images/err.png";
		return false;
	}
	textobj.style.backgroundColor = "#fff";
	imgobj.src = "/static/images/ok.png";
	passtmp = textobj.value;
	return true;
}

function checkRpass(imgobj,textobj){
	imgobj.style.visibility = "visible";
	textobj.value = trim(textobj.value);
	if(textobj.value.length<6 || passtmp!=textobj.value){
		textobj.style.backgroundColor = "#AAAAAA";
		imgobj.src = "/static/images/err.png";
		return false;
	}
	textobj.style.backgroundColor = "#fff";
	imgobj.src = "/static/images/ok.png"; 
	return true;
}

function checkEmail(imgobj,textobj){
	imgobj.style.visibility = "visible";
	textobj.value = trim(textobj.value);
	if(textobj.value.match(/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/)==null){
		textobj.style.backgroundColor = "#AAAAAA";
		imgobj.src = "/static/images/err.png";
		return false;
	}
	textobj.style.backgroundColor = "#fff";
	imgobj.src = "/static/images/ok.png";
	return true;
}

function checkTel(imgobj,textobj){
	imgobj.style.visibility = "visible";
	textobj.value = trim(textobj.value);
	if(textobj.value.match(/[^\d]+/g)!=null){
		textobj.style.backgroundColor = "#AAAAAA";
		imgobj.src = "/static/images/err.png";
		return false;
	}
	if(textobj.value.match(/\d+/g)!=null && textobj.value.match(/\d+/g)[0].length == 11){
		textobj.style.backgroundColor = "#fff";
		imgobj.src = "/static/images/ok.png";
		return true;
	}
	textobj.style.backgroundColor = "#AAAAAA";
	imgobj.src = "/static/images/err.png";
	return false;
}

function checkQq(imgobj,textobj){
	imgobj.style.visibility = "visible";
	textobj.value = trim(textobj.value);
	if(textobj.value.match(/[^\d]+/g)!=null){
		textobj.style.backgroundColor = "#AAAAAA";
		imgobj.src = "/static/images/err.png";
		return false;
	}
	textobj.style.backgroundColor = "#fff";
	imgobj.src = "/static/images/ok.png";
	return true;
}