/*=======================================================================
Content  : FormLoad
========================================================================*/
$(document).ready(function() {
	try {
		Schedule.PageLoad();
	}
	catch (e) { console.log(e.message); }
});

/*=======================================================================
Content  : FormBeforeUnLoad
========================================================================*/
function FormBeforeUnLoad() {
	try {
	}
	catch (e) { console.log(e.message); }
}

/*=======================================================================
Schedule Class 명세 시작 (상수(변수)>>속성>>메서드)
========================================================================*/
//Schedule Class
var Schedule = {};

//Schedule Variable

Schedule.PageLoad = function() { };  //메인 페이지 로드 공통 함수
Schedule.SetEvent = function() { };  //메인 페이지 이벤트 바인딩
/*=======================================================================
Schedule Class 명세 끝
========================================================================*/

/*=======================================================================
내      용  : 메인 페이지 로드(PageLoad)
2022.07.11 - 최초생성
========================================================================*/
Schedule.PageLoad = function() {
	try {
		Schedule.Init();
		Schedule.SetEvent();
	}
	catch (e) { console.log(e.message); }
}

/*=======================================================================
내      용  : 메인 페이지 초기화
2022.07.11 - 최초생성
========================================================================*/
Schedule.Init = function() {
	try {
		$(window).scroll(function() {
			if (Math.round($(window).scrollTop())+30 >= $(document).height() - $(window).height()) {
				$(".i-down").fadeOut();
			}else {
				$(".i-down").fadeIn();
			}
		});
	}
	catch (e) { console.log(e.message); }
}

/*=======================================================================
내      용  : 이벤트 핸들러
작  성  자  : 김진열
2022.07.11 - 최초생성
========================================================================*/
Schedule.SetEvent = function() {
	try {
    	// 뒤로가기
    	$("#iBack").click(function(e) {
    		location = document.referrer;
    	});
    	
    	// 아래 스크롤
    	$(".i-down").click(function(e) {
			$('html, body').animate({scrollTop:document.body.scrollHeight}, '300');
		});
    	
	}
	catch (e) {
		console.log(e.message);
	}
}