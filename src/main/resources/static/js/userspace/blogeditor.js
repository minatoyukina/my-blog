/*!
 * blogedit.html 页面脚本.
 *
 * @since: 1.0.0 2017-03-26
 * @author Way Lau <https://waylau.com>
 */
"use strict";
//# sourceURL=blogeditor.js

// DOM 加载完再执行
$(function () {

    // 初始化 md 编辑器
    editormd("test-editormd", {
        width: "100%",
        height: 500,
        syncScrolling: "single",
        //你的lib目录的路径，我这边用JSP做测试的
        tocm: true, // Using [TOCM]
        tex: true, // 开启科学公式TeX语言支持，默认关闭
        flowChart: true, // 开启流程图支持，默认关闭
        path: "/editormd/lib/",

        imageUpload: true,
        imageFormats: ["jpg", "jpeg", "gif", "png", "bmp", "webp"],
        imageUploadURL: "upload",
        //这个配置在simple.html中并没有，但是为了能够提交表单，使用这个配置可以让构造出来的HTML代码直接在第二个隐藏的textarea域中，方便post提交表单。
        saveHTMLToTextarea: true,

        watch: false
    });

    $(".editormd-preview-close-btn").hide();

    // 初始化下拉
    $('.form-control-chosen').chosen();


    // 初始化标签
    $('.form-control-tag').tagsInput({
        'defaultText': '输入标签'

    });

    // 发布博客
    $("#submitBlog").click(function () {
        // 获取 CSRF Token
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");

        $.ajax({
            url: '/u/' + $(this).attr("userName") + '/blogs/edit',
            type: 'POST',
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify({
                "id": $('#blogId').val(),
                "title": $('#title').val(),
                "summary": $('#summary').val(),
                "content": $('#content').val(),
                "htmlContent": $('#htmlContent').val(),
                "catalog": {"id": $('#catalogSelect').val()},
                "tags": $('.form-control-tag').val()
            }),
            beforeSend: function (request) {
                request.setRequestHeader(csrfHeader, csrfToken); // 添加  CSRF Token
            },
            success: function (data) {
                if (data.success) {
                    // 成功后，重定向
                    window.location = data.body;
                } else {
                    toastr.error("error!" + data.message);
                }

            },
            error: function () {
                toastr.error("error!");
            }
        })
    })


});