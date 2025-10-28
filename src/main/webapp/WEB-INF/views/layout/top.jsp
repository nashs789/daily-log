<%@ page contentType="text/html; charset=UTF-8" %>

<div class="topbar">
    <button class="hamburger"
            type="button"
            data-menu-toggle
            aria-controls="sidebar"
            aria-expanded="false"
            aria-label="메뉴 열기"
            style="background:#ff4757;border:0;color:#fff;padding:0 10px;border-radius:8px;">☰</button>
    <div class="brand"><a href="/">LifeLog</a></div>
    <div style="flex:1"></div>
    <div>
        <a href="${lifelog.app.base}/auth/login" style="color:#fff">Login</a>
    </div>
</div>

<!-- 어둡게 가리는 오버레이 -->
<div class="backdrop" data-menu-close aria-hidden="true"></div>

<script src="${lifelog.app.script.jquery}"></script>

<script>
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

        // (선택) 오버레이 말고도 바깥을 클릭하면 닫히도록
        $(document).on('click', function(e){
            if ($body.hasClass('menu-open')) {
                if (!$(e.target).closest('#sidebar, [data-menu-toggle]').length) {
                    closeMenu();
                }
            }
        });
    });
</script>
