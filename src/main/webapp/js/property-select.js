$('#select_input').change(function(){ 
	modify();
})

function modify(){
	$('#input_select').val($('#select_input').val());
	output();
}

function output(){

}

$('#input').on('click', function(){
	$(this).select()
}).on('blur', function(){
	output();
})

modify();