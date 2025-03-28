
window.onload = function () {
    fillTable();
}

// Bootstrap 5 modal initialization
let editUserModal;
let deleteUserModal;

document.addEventListener('DOMContentLoaded', function() {
    editUserModal = new bootstrap.Modal(document.getElementById('editUserModal'));
    deleteUserModal = new bootstrap.Modal(document.getElementById('deleteUserModal'));
});

function fillTable() {
    const table = document.getElementById("usersTableBody");
    let result = '';
    fetch('/admin/getAllUsers')
        .then(response => response.json())
        .then(users => {
            for (const i in users) {
                let roleList = '';
                users[i].roles.forEach(role => roleList += role.name.replace("ROLE_", "") + ' ');
                result += `<tr id="${users[i].id}">
                    <td>${users[i].id}</td>
                    <td>${users[i].username}</td>
                    <td>${users[i].lastname}</td>
                    <td>${users[i].age}</td>
                    <td>${users[i].email}</td>
                    <td>${roleList}</td>
                    <td>
                        <button type="button" class="btn btn-info btn-sm text-white" onclick="editUser(${users[i].id})">Edit</button>
                    </td>
                    <td>
                        <button type="button" class="btn btn-danger btn-sm" onclick="deleteUser(${users[i].id})">Delete</button>
                    </td>
                    </tr>`;
            }
            table.innerHTML = result;
        });
}

function deleteUser(id) {
    let row = document.getElementById(id);
    let cells = row.childNodes;
    let form = document.getElementById("delete_user");
    let result = '';
    result += `
        <div class="mb-3">
            <label for="id_delete" class="form-label">ID</label>
            <input readonly id="id_delete" type="number" class="form-control" value="${cells[1].textContent}" name="id">
        </div>
        <div class="mb-3">
            <label for="firstName_delete" class="form-label">FirstName</label>
            <input readonly type="text" class="form-control" value="${cells[3].textContent}" name="username">
        </div>
        <div class="mb-3">
            <label for="lastName_delete" class="form-label">LastName</label>
            <input readonly type="text" class="form-control" value="${cells[5].textContent}" name="lastname">
        </div>
        <div class="mb-3">
            <label for="age_delete" class="form-label">Age</label>
            <input readonly type="number" class="form-control" value="${cells[7].textContent}" name="age">
        </div>
        <div class="mb-3">
            <label for="email_delete" class="form-label">Email</label>
            <input readonly type="email" class="form-control" value="${cells[9].textContent}" name="email" aria-describedby="emailHelp">
        </div>
        <div class="mb-3">
            <label for="role_delete" class="form-label">Roles</label>
            <input readonly type="text" class="form-control" value="${cells[11].textContent}" name="role">
        </div>`;
    form.innerHTML = result;
    deleteUserModal.show();
}

function editUser(id) {
    let result = '';
    let row = document.getElementById(id);
    let cells = row.childNodes;
    let form = document.getElementById("edit_user");

    result += `
        <div class="mb-3">
            <label for="id_edit" class="form-label">ID</label>
            <input readonly type="number" class="form-control" id="id_edit" value="${cells[1].textContent}" name="id">
        </div>
        <div class="mb-3">
            <label for="firstName_edit" class="form-label">FirstName</label>
            <input type="text" id="firstName_edit" class="form-control" value="${cells[3].textContent}" name="username" required>
        </div>
        <div class="mb-3">
            <label for="lastName_edit" class="form-label">LastName</label>
            <input type="text" id="lastName_edit" class="form-control" value="${cells[5].textContent}" name="lastname" required>
        </div>
        <div class="mb-3">
            <label for="age_edit" class="form-label">Age</label>
            <input type="number" id="age_edit" class="form-control" value="${cells[7].textContent}" name="age" required>
        </div>
        <div class="mb-3">
            <label for="email_edit" class="form-label">Email</label>
            <input type="email" id="email_edit" class="form-control" value="${cells[9].textContent}" name="email" aria-describedby="emailHelp" required>
        </div>
        <div class="mb-3">
            <label for="password_edit" class="form-label">Password</label>
            <input type="password" id="password_edit" class="form-control" name="password">
        </div>
        <div class="mb-3">
            <label for="role_edit" class="form-label">Roles</label>
            <select id="role_edit" name="roles" multiple class="form-select" size="2" aria-label="size 2 select" required>
                <option value="ROLE_USER">USER</option>
                <option value="ROLE_ADMIN">ADMIN</option>
            </select>
        </div>`;
    form.innerHTML = result;
    editUserModal.show();
}

function confirmDelete() {
    let id = document.getElementById("id_delete").getAttribute("value");
    fetch("/admin/delete/" + id, {
        method: 'DELETE'
    }).then(function () {
        deleteUserModal.hide();
        clearTable();
        fillTable();
    });
}

function confirmEdit() {
    let role_edit = document.getElementById("role_edit");
    fetch('/admin/update', {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify({
            id: document.getElementById("id_edit").value,
            username: document.getElementById("firstName_edit").value,
            lastname: document.getElementById("lastName_edit").value,
            age: document.getElementById("age_edit").value,
            email: document.getElementById("email_edit").value,
            password: document.getElementById("password_edit").value,
            roles: Array.from(role_edit.options)
                .filter(option => option.selected)
                .map(option => option.value)
        })
    }).then(function () {
        editUserModal.hide();
        clearTable();
        fillTable();
    }).catch(error => console.log(error));
}

function clearTable() {
    const table = document.getElementById("usersTableBody");
    table.innerHTML = "";
}

function addNewUser() {
    const role_select = document.getElementById("role-select");
    const user_info = Array.from(document.getElementsByClassName("add"));

    // Проверка заполненности полей
    if (!role_select || user_info.some(field => !field.value.trim())) {
        alert("Заполните все поля!");
        return;
    }

    const user = {
        username: user_info[0].value,
        lastname: user_info[1].value,
        age: parseInt(user_info[2].value), // Преобразование в число
        email: user_info[3].value,
        password: user_info[4].value,
        // roles: Array.from(role_select.selectedOptions)
        //     .map(option => String(option.value))
        //     .map(option : role)
    };
    class Role {
        constructor(name) {
            this.name = name;
        }
    }
    user.roles = Array.from(role_select.selectedOptions)
        .map(option => new Role(String(option.value)));

    fetch('/admin/create', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json;charset=utf-8' },
        body: JSON.stringify(user)
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => { throw new Error(text) });
            }
            return response.json();
        })
        .then(() => {
            // document.getElementById("all-users-tab").click();
            document.getElementById('newUserForm').reset();
            const allUsersTab = new bootstrap.Tab(document.getElementById('all-users-tab'));
            allUsersTab.show();
            clearTable();
            fillTable();
        })
        .catch(error => {
            // console.error("Ошибка:", error);
            // alert("Ошибка при создании: " + error.message);
        });
}
