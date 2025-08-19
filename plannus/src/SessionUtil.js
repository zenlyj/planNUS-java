const session = {
    save(key, value) {
        return localStorage.setItem(key, value)
    },

    get(key) {
        return localStorage.getItem(key)
    },

    clear() {
        localStorage.clear()
    },

    studentId() {
        return localStorage.getItem("student_id")
    },

    accessToken() {
        return localStorage.getItem("access_token")
    },

    refreshToken() {
        return localStorage.getItem("refresh_token")
    }
}

export default session