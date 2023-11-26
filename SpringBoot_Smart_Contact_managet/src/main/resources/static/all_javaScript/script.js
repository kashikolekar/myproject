console.log("this is Script file")

const toggleSidebar =()=>{


    if($(".sidebar").is(":visible"))
    {
            

        $(".sidebar").css("display","none")
        $(".content").css("margin-left","0%")
    }else{
        $(".sidebar").css("display","block")
        $(".content").css("margin-left","20%")

    }
};

const deletecontact=(cid)=>{
	swal({
				title: "Are you sure?",
				text: "Once deleted, you will not be able to recover this imaginary file!",
				icon: "warning",
				buttons: true,
				dangerMode: true,
			})
				.then((willDelete) => {
					if (willDelete) {
						
						window.location="/user/delete/"+cid;
						
					} else {
						swal("Your Conatct is  safe!");
					}
				});
};
