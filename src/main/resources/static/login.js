const openRegisterButton = document.getElementById('openRegisterButton');
const registerForm = document.getElementById('registerForm');
const carousel = document.getElementById('carousel');
const alertDiv = document.getElementById('alertDiv');
const loginForm = document.getElementById('loginForm');
const LOGIN_SLIDE = 0,
    REGISTER_SLIDE = 1;

document.addEventListener('DOMContentLoaded', () => {
    $(carousel).carousel('pause');

    openRegisterButton.addEventListener('click', e => {
        e.preventDefault();
        $(carousel).carousel(REGISTER_SLIDE);
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
});

function showAlert(message, type) {
    alertDiv.innerHTML = `<div class="alert alert-${type}" role="alert">${message}</div>`;
}