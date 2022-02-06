// (1) 회원정보 수정
function update(userId, event) {
    event.preventDefault(); // 폼태그액션 막기

    let data = $("#profileUpdate").serialize();
    <!-- serialize하면 값이 담기게 됨. key=value -->

    console.log(data);

    <!-- ajax는 data 응답 -->
    $.ajax({
        type:"put",
        url:`/api/user/${userId}`,
        data:data,
        contentType:"application/x-www-form-urlencoded; charset=utf-8",
        dataType:"json"
    }).done(res=>{  // HttpStatus 200
        console.log("success!!", res);
        location.href=`/user/${userId}`;
    }).fail(error=>{    // HttpStatus 200 아닐때
        if(error.data==null){
            alert(error.responseJSON.message);
        } else {
            alert(JSON.stringify(error.responseJSON.data));
        }

    });
}