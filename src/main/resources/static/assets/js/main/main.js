/*=======================================================================
Content  : FormLoad
========================================================================*/
$(document).ready(function() {
	try {
		Main.PageLoad();
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
Main Class 명세 시작 (상수(변수)>>속성>>메서드)
========================================================================*/
//Main Class
var Main = {};

//Main Variable

Main.PageLoad = function() { };  //메인 페이지 로드 공통 함수
Main.SetEvent = function() { };  //메인 페이지 이벤트 바인딩
/*=======================================================================
Main Class 명세 끝
========================================================================*/

/*=======================================================================
내      용  : 메인 페이지 로드(PageLoad)
2022.07.11 - 최초생성
========================================================================*/
Main.PageLoad = function() {
	try {
		Main.Init();
		Main.SetEvent();
	}
	catch (e) { console.log(e.message); }
}

/*=======================================================================
내      용  : 메인 페이지 초기화
2022.07.11 - 최초생성
========================================================================*/
Main.Init = function() {
	try {
		Main.GetMaker();
	}
	catch (e) { console.log(e.message); }
}

/*=======================================================================
내      용  : 페이징
2022.07.11 - 최초생성
========================================================================*/
Main.Paging = function(page) {
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
Main.SetEvent = function() {
	try {
		// 차량 제조사 선택
    	$("#selCarMaker").change(function(e) {
    		Main.GetBrand($("#selCarMaker").val());
    	});
    	
    	// 금리계산
    	$("#btnRateCalc").click(function(e) {
    		Main.RateCalc();
    	});
    	
    	// 초기화
    	$("#btnReset").click(function(e) {
    		$("input").val("");
    	});
	}
	catch (e) {
		console.log(e.message);
	}
}

/*=======================================================================
내      용  : 차량 제조사 조회
작  성  자  : 김진열
2022.07.11 - 최초생성
========================================================================*/
Main.GetMaker = function() {
	try {
		$.ajax({
			type : "get",
			url : "/v1/api/get/car_info/maker",
			success : function(json){
				if(json.result == "success") {
					var makerArray = json.makerList;
					
					makerArray.sort((a, b) => {
					    if (a.sort < b.sort) return -1;
					    if (a.sort > b.sort) return 1;
					    return 0;
					});
					
					for(var maker of makerArray) {
						if(sCarMaker == maker.name) {
							var option = $("<option selected='selected'>"+maker.name+"</option>");
							Main.GetBrand(maker.name);
						}else {
							var option = $("<option>"+maker.name+"</option>");
						}
						$("#selCarMaker").append(option);
					}
				}else {
					alert(json.msg);
				}
			},
			error: function(request,status,error,data){
				alert("잘못된 접근 경로입니다.");
				return false;
			}
		});
	}
	catch (e) {
		console.log(e.message);
	}
}

/*=======================================================================
내      용  : 차량 브랜드 조회
작  성  자  : 김진열
2022.07.11 - 최초생성
========================================================================*/
Main.GetBrand = function(maker) {
	try {
		if(maker != "차량 제조사") {
			$.ajax({
				type : "get",
				url : "/v1/api/get/car_info/brand/" + maker,
				success : function(json){
					if(json.result == "success") {
						$("#selCarBrand option").remove();
						
						var brandArray = json.brandList;
						
						brandArray.sort((a, b) => {
						    if (a.sort < b.sort) return -1;
						    if (a.sort > b.sort) return 1;
						    return 0;
						});
						
						for(var brand of brandArray) {
							if(sCarBrand == brand.name) {
								var option = $("<option selected='selected'>"+brand.name+"</option>");
							}else {
								var option = $("<option>"+brand.name+"</option>");
							}
							$("#selCarBrand").append(option);
						}
					}else {
						alert(json.msg);
					}
				},
				error: function(request,status,error,data){
					alert("잘못된 접근 경로입니다.");
					return false;
				}
			});
		}else {
			$("#selCarBrand option").remove();
			var option = $("<option>차량 종류 (제조사를 선택해주세요)</option>");
			$("#selCarBrand").append(option);
		}
	}
	catch (e) {
		console.log(e.message);
	}
}

/*=======================================================================
내      용  : 금리 조회 페이지 이동
작  성  자  : 김진열
2022.07.11 - 최초생성
========================================================================*/
Main.RateCalc = function() {
	try {
		if(Main.ValidationCheck()) {
			$("#rateForm").submit();
		}
	}
	catch (e) {
		console.log(e.message);
	}
}

/*=======================================================================
내      용  : 콤마 생성
작  성  자  : 김진열
2022.07.11 - 최초생성
========================================================================*/
Main.comma = function(input) {
	try {
		str = $(input).val();
		str = Main.uncomma(str);
		str = str.replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
        $(input).val(str);
	}
	catch (e) {
		console.log(e.message);
	}
}

/*=======================================================================
내      용  : 콤마 삭제
작  성  자  : 김진열
2022.07.11 - 최초생성
========================================================================*/
Main.uncomma = function(str) {
	try {
		str = str.replace(/[^\d]+/g, '');
        return str;
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
Main.ValidationCheck = function() {
	try {
		var flag = true;
		
		$(".validation_red").removeClass("validation_red");
		
		if($("#selCarMaker").val() == "차량 제조사") {
			flag = false;
			alert("차량 제조사를 선택해주세요");
			$("#selCarMaker").parent().addClass("validation_red");
		}
		
		else if($("#selCarBrand").val() == "차량 종류") {
			flag = false;
			alert("차량 종류를 선택해주세요");
			$("#selCarBrand").parent().addClass("validation_red");
		}
		
		else if($("#acquisitionCost").val() == "" || $("#acquisitionCost").val() == 0) {
			flag = false;
			alert("취득원가를 입력해주세요");
			$("#acquisitionCost").focus();
			$("#acquisitionCost").parent().parent().addClass("validation_red");
		}
		
		/*
		else if($("#residualValue").val() == "") {
			flag = false;
			alert("잔존가치를 입력해주세요");
			$("#residualValue").focus();
		}
		*/
		
		else if($("#leaseCost").val() == "" || $("#leaseCost").val() == 0) {
			flag = false;
			alert("월 리스료를 입력해주세요");
			$("#leaseCost").focus();
			$("#leaseCost").parent().parent().addClass("validation_red");
		}
				
		else if($("#leaseMonth").val() == "" || $("#leaseMonth").val() == 0) {
			flag = false;
			alert("리스기간을 입력해주세요");
			$("#leaseMonth").focus();
			$("#leaseMonth").parent().parent().addClass("validation_red");
		}
		
		else {
			var inputs = $("input");
			
			for(var input of inputs) {
				var value = $(input).val().replace(/[^\d]+/g, '');
				if(value == "") {
					$(input).val(0);
				}else {
					$(input).val(value);
				}
			}
		}
		
		return flag;
	}
	catch (e) {
		console.log(e.message);
	}
}