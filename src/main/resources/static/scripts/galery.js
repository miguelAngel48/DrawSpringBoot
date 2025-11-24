const galery = document.querySelector('#galery');
const refresh = document.querySelector('#refresh');
const listUsers = document.getElementById("listUsers");
const others = document.querySelectorAll('.others');


function handleUserFilter() {

    if (listUsers.value === 'userDraw') {

        others.forEach(row => {
            row.classList.add('hidden');

        });

    } else {

        others.forEach(row => {
            row.classList.remove('hidden');
        });

    }
}


listUsers.addEventListener('change', handleUserFilter);
document.addEventListener('DOMContentLoaded', handleUserFilter);