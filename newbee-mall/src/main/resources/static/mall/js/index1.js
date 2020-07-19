/*---------------------Swiper 插件的初始化---------------------*/
/*对 class 为scrollHeightwiper-container 的 DOM 对象进行轮播效果的初始化，之后
设置自动播放，以及自动播放的间隔时间为 2 秒，同时也设置了手动轮播的按钮*/
var newbeeSwiper = newscrollHeightwiper('.swiper-container', {
    //自动播放
    autoplay: {
        delay: 2000,
        disableOnIteraction: false
    },
    //无限循环
    loop: true,
    //圆点指示器
    pagination: {
        el: '.swiper-pagination',
    },
    //设置上下页
    navigation: {
        nextEl: '.swiper-button-next',
        preEl: '.swiper-button-prev'
    }
})
/*---------------------商品分类处理---------------------*/
$('.all-sort-list>.item').hover(function () {
    var currentItemIndex = $('.all-sort-list > .item').index(this),				//获取当前滑过是第几个元素
        distanceFromListToTop = $('.all-sort-list').offset().top,				//获取当前下拉菜单距离窗口多少像素
        scrollHeight = $(window).scrollTop(),									//获取浏览器滚动了多少高度
        pixelFromItemToTop = $(this).offset().top,								//当前元素滑过距离窗口多少像素
        itemHeight = $(this).children('.item-list').height(),				    //下拉菜单子类内容容器的高度
        listHeight = $('.all-sort-list').height();						        //父类分类列表容器的高度

    if (itemHeight < listHeight) {                                              //如果子类的高度小于父类的高度
        if (currentItemIndex == 0) {
            $(this).children('.item-list').css('top', (pixelFromItemToTop - distanceFromListToTop));
        } else {
            $(this).children('.item-list').css('top', (pixelFromItemToTop - distanceFromListToTop) + 1);
        }
    } else {
        if (scrollHeight > distanceFromListToTop) {            //判断子类的显示位置，如果滚动的高度大于所有分类列表容器的高度
            if (pixelFromItemToTop - scrollHeight > 0) {       //则继续判断当前滑过容器的位置 是否有一半超出窗口一半在窗口内显示的Bug,
                $(this).children('.item-list').css('top', (scrollHeight - distanceFromListToTop) + 2);
            } else {
                $(this).children('.item-list').css('top', (scrollHeight - distanceFromListToTop) - (-(pixelFromItemToTop - scrollHeight)) + 2);
            }
        } else {
            $(this).children('.item-list').css('top', 3);
        }
    }
    $(this).addClass('hover');
    $(this).children('.item-list').css('display', 'block');
}, function () {
    $(this).removeClass('hover');
    $(this).children('.item-list').css('display', 'none');
});


