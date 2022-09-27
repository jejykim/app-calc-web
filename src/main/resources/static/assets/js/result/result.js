/*=======================================================================
Content  : FormLoad
========================================================================*/
$(document).ready(function() {
	try {
		Result.PageLoad();
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
Result Class 명세 시작 (상수(변수)>>속성>>메서드)
========================================================================*/
//Result Class
var Result = {};

//Result Variable

Result.PageLoad = function() { };  //메인 페이지 로드 공통 함수
Result.SetEvent = function() { };  //메인 페이지 이벤트 바인딩
/*=======================================================================
Result Class 명세 끝
========================================================================*/

/*=======================================================================
내      용  : 메인 페이지 로드(PageLoad)
2022.07.11 - 최초생성
========================================================================*/
Result.PageLoad = function() {
	try {
		Result.Init();
		Result.SetEvent();
	}
	catch (e) { console.log(e.message); }
}

/*=======================================================================
내      용  : 메인 페이지 초기화
2022.07.11 - 최초생성
========================================================================*/
Result.Init = function() {
	try {
		var article = (".result-nav table .show");
		$(".result-nav table .sub-content td").click(function () {
			var myArticle = $(this).parents().next("tr");
			if ($(myArticle).hasClass('hide')) {
				$(article).removeClass('show').addClass('hide');
				$(myArticle).removeClass('hide').addClass('show');
			} else {
				$(myArticle).addClass('hide').removeClass('show');
			}
		});
		
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
내      용  : 페이징
2022.07.11 - 최초생성
========================================================================*/
Result.Paging = function(page) {
	try {
		if (page > 0) {
			$("#now_page").val(page);
			document.searchForm.submit();
		} else {
			alert("잘못된 경로 입니다");
		}	
	}
	catch (e) { 
		console.log(e.message); 
	}
}

/*=======================================================================
내      용  : 이벤트 핸들러
작  성  자  : 김진열
2022.07.11 - 최초생성
========================================================================*/
Result.SetEvent = function() {
	try {
    	// 뒤로가기
    	$("#iBack").click(function(e) {
    		location.href = "/";
    	});
    	
    	// 아래 스크롤
    	$(".i-down").click(function(e) {
			$('html, body').animate({scrollTop:document.body.scrollHeight}, '300');
		});
    	
    	// 모달 열기
    	$("#btnPopup").click(function(e) {
    		$(".popup").fadeIn(100);
    	});
    	
    	// 모달 닫기
    	$("#btnCancelPopup").click(function(e) {
    		$(".popup").fadeOut(100);
    	});
    	
    	// 연락처 남기기
    	$("#btnColTel").click(function(e) {
    		Result.CollectTel();
    	});
    	
    	// 상환스캐쥴표 이동
    	$("#btnSchedule").click(function(e) {
    		Result.LinkSchedule();
    	});
    	
	}
	catch (e) {
		console.log(e.message);
	}
}

/*=======================================================================
내      용  : 연락처 수집
작  성  자  : 김진열
2022.07.21 - 최초생성
========================================================================*/
Result.CollectTel = function() {
	try {
		if(Result.ValidationCheck()) {
			
			var tel = $("#mTel").val();
			
			var data = {
				"carMaker" : $("#carMaker").val()
				, "carBrand" : $("#carBrand").val()
				, "leaseMonth" : $("#leaseMonth").val()
				, "leaseCost" : $("#leaseCost").val()
				, "advancePayment" : $("#advancePayment").val()
				, "acquisitionCost" : $("#acquisitionCost").val()
				, "residualValue" : $("#residualValue").val()
				, "deposit" : $("#deposit").val()
				, "deposit2" : $("#deposit2").val()
				, "extra" : $("#extra").val()
				, "tel" : tel
				, "rate" : $("#rate").val()
			};
			
			$.ajax({
				type : "post",
				url : "/v1/api/collect/tel",
				data : JSON.stringify(data),
				contentType: 'application/json',
				success : function(json){
					if(json.result == "success") {
						alert("참여해주셔서 감사합니다.\n더 좋은 금리로 안내드리겠습니다.");
						$(".popup").fadeOut(100);
					}else {
						console.log(json.msg);
					}
				},
				error: function(request,status,error,data){
					alert("잘못된 접근 경로입니다.");
					return false;
				}
			});
		}
	}
	catch (e) {
		console.log(e.message);
	}
}

/*=======================================================================
내      용  : 유효성 검사
작  성  자  : 김진열
2022.07.11 - 최초생성
========================================================================*/
Result.ValidationCheck = function() {
	try {
		var flag = true;
		
		if($("#mTel").val() == "" || $("#mTel").val() == "undefined") {
			flag = false;
			alert("연락처를 입력해주세요");
			$("#mTel").focus();
		}
		
		else if($("#mTel").val().length < 12) {
			flag = false;
			alert("올바른 연락처를 입력해주세요");
			$("#mTel").focus();
		}
		
		else if(!$("#checkTerm").is(":checked")) {
			alert("연락처 수집 동의를 체크해주세요");
			$("#checkTerm").focus();
			flag = false;
		}
		
		return flag;
	}
	catch (e) {
		console.log(e.message);
	}
}

/*=======================================================================
내      용  : 연락처 정규식
작  성  자  : 김진열
2022.07.22 - 최초생성
========================================================================*/
Result.Tel_validation = function(input) {
	try {
		var tel = $(input).val();
		tel = tel.replace(/[^0-9]/g, '');
		$(input).val(tel.replace(/^(\d{2,3})(\d{3,4})(\d{4})$/, `$1-$2-$3`));
	}
	catch (e) {
		console.log(e.message);
	}
}

/*=======================================================================
내      용  : 상환스캐쥴표 이동
작  성  자  : 김진열
2022.07.22 - 최초생성
========================================================================*/
Result.LinkSchedule = function() {
	try {
		$("#scheduleForm").submit();
	}
	catch (e) {
		console.log(e.message);
	}
}