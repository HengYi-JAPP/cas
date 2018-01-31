$(document).ready(function () {

    $('.Interface .key').focus(function () {
        $(this).siblings().hide()
    })


    $('.Interface .key').blur(function () {
        var ovla = $(this).val();
        if (ovla == '') {
            $(this).siblings().show()
        }
        else {
            $(this).siblings().hide()
        }

    })

    $('.Interface .key').each(function () {
        var ovla = $(this).val();
        if (ovla == '') {
            $(this).siblings().show()
        }
        else {
            $(this).siblings().hide()
        }
    })


    var currClientWidth, fontValue, originWidth;
    originWidth = 375;
    __resize();

    window.addEventListener('resize', __resize, false);

    function __resize() {
        currClientWidth = document.documentElement.clientWidth;
        if (currClientWidth > 640) currClientWidth = 640;
        if (currClientWidth < 320) currClientWidth = 320;
        fontValue = ((62.5 * currClientWidth) / originWidth).toFixed(2);
        document.documentElement.style.fontSize = fontValue + '%';

    }

})


$(document).ready(function () {
    var onscreen = window.screen.width,
        $link = $("link[href$='max.css'],link[href$='maxwap.css']").first(),
        alink = $link.attr('href'),
        alink = alink.substring(0, alink.lastIndexOf('/') + 1);
    if (onscreen < 800) {
        $link.attr('href', alink + 'maxwap.css')
    } else {
        $link.attr('href', alink + 'max.css')
    }
})
