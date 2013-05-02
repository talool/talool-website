$(function () {
    'use strict';

    $('#${componentMarkupId}').fileupload({
        url: '${url}',
        paramName: '${paramName}',
        singleFileUploads: true,
        maxFileSize: 5000000,
        acceptFileTypes: /(\.|\/)(gif|jpe?g|png)$/i,
        process: [
            {
                action: 'load',
                fileTypes: /^image\/(gif|jpeg|png)$/,
                maxFileSize: 20000000 // 20MB
            },
            {
                action: 'resize',
                maxWidth: 1440,
                maxHeight: 900
            },
            {
                action: 'save'
            }
        ]
    }).bind('fileuploaddone', function(e, data){
    	var resp = eval(data.jqXHR.responseText);
    	/*
    	[{"thumbnail_url":"./fileManager?filename=centro_3.png",
    		"delete_url":"./fileManager?filename=centro_3.png&delete=true",
    		"delete_type":"POST",
    		"name":"centro_3.png",
    		"url":"./fileManager?filename=centro_3.png",
    		"size":84936}]
    	*/
    	parent.window.callDealUploadComponent(resp[0].url);
    });

});
