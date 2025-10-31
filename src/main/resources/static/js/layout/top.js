$(function () {
    var $body    = $('body');
    var $sidebar = $('#sidebar');                 // layout/leftMenu.jsp 에 id="sidebar"
    var $toggles = $('[data-menu-toggle]');
    var $closers = $('[data-menu-close]');

    function openMenu(){
        $body.addClass('menu-open');
        setAria(true);
    }

    function closeMenu() {
        $body.removeClass('menu-open'); setAria(false);
    }

    function toggleMenu() {
        $body.hasClass('menu-open') ? closeMenu() : openMenu();
    }

    function setAria(expanded){
        $toggles.attr('aria-expanded', String(expanded));
        if (expanded) {
            $sidebar.attr('tabindex','-1').trigger('focus');
        } else {
            $sidebar.removeAttr('tabindex');
        }
    }

    // 이벤트 바인딩
    $(document).on('click', '[data-menu-toggle]', function(e) {
        e.preventDefault(); toggleMenu();
    });

    $(document).on('click', '[data-menu-close]', function(e) {
        e.preventDefault(); closeMenu();
    });

    $(window).on('keydown', function(e) {
        if (e.key === 'Escape') closeMenu();
    });

    $(window).on('resize', function() {
        closeMenu();
    });

    $(document).on('click', function(e){
        if ($body.hasClass('menu-open')) {
            if (!$(e.target).closest('#sidebar, [data-menu-toggle]').length) {
                closeMenu();
            }
        }
    });
});