const openRegisterButton = document.getElementById('openRegisterButton');
const registerForm = document.getElementById('registerForm');
const carousel = document.getElementById('carousel');
const alertDiv = document.getElementById('alertDiv');
const loginForm = document.getElementById('loginForm');
const registerBackButton = document.getElementById('registerBackButton');
const loginModal = document.getElementById('loginModal');
const LOGIN_SLIDE = 0,
    REGISTER_SLIDE = 1,
    TELEGRAM_SLIDE = 2;

document.addEventListener('DOMContentLoaded', () => {
    $(carousel).carousel('pause');
    $(carousel).on('slide.bs.carousel', () => alertDiv.innerHTML = '');

    $.ajaxSetup({
        beforeSend: () => loginModal.classList.add('fullscreen-loading-modal'),
        complete: () => loginModal.classList.remove('fullscreen-loading-modal')
    });

    let socket = new SockJS('/websocket');
    let stompClient = Stomp.over(socket);

    let headers = {};
    let csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;
    headers[csrfHeader] = document.querySelector('meta[name="_csrf"]').content;

    stompClient.connect(headers, frame => {
        console.log(frame);

        stompClient.subscribe('/user/queue/login', data => {
            let info = JSON.parse(data.body);

            if (info.success) {
                loginModal.classList.add('fullscreen-loading-modal');
                location.href = info.redirectUrl;
            } else {
                loginForm.querySelector('input[name="password"]').value = '';
                $(carousel).carousel(LOGIN_SLIDE);
                showAlert(info.errorMessage, 'danger');
            }
        });
    });

    loginForm.addEventListener('submit', e => {
        e.preventDefault();

        $.ajax({
            method: 'POST',
            url: '/login',
            data: $(loginForm).serialize(),
            error: response => {
                let data = response.responseJSON;
                if (data.requiredMfas && data.requiredMfas.includes('TELEGRAM_MFA')) {
                    $(carousel).carousel(TELEGRAM_SLIDE);
                } else {
                    showAlert(data.errorMessage, 'danger');
                    loginForm.querySelector('input[name="password"]').value = '';
                }
            }
        }).done(response => {
            loginModal.classList.add('fullscreen-loading-modal');
            location.href = response.redirectUrl;
        });
    });

    registerForm.addEventListener('submit', e => {
        e.preventDefault();

        const form = e.target;
        $.ajax({
            method: 'POST',
            url: '/ajax/user/register',
            contentType: 'application/json',
            data: JSON.stringify({
                name: form.querySelector('input[name="name"]').value,
                username: form.querySelector('input[name="username"]').value,
                password: form.querySelector('input[name="password"]').value
            })
        }).done(response => {
            loginForm.querySelector('input[name="username"]').value = response.username;
            showAlert('Вы успешно зарегестрировались', 'success');
            $(carousel).carousel(LOGIN_SLIDE);
        });
    });

    openRegisterButton.addEventListener('click', e => {
        e.preventDefault();
        $(carousel).carousel(REGISTER_SLIDE);
    });

    registerBackButton.addEventListener('click', e => {
        e.preventDefault();
        $(carousel).carousel(LOGIN_SLIDE);
    });
});

function showAlert(message, type) {
    alertDiv.innerHTML = `<div class="alert alert-${type}" role="alert">${message}</div>`;
}