function refreshBars() {
    const barsList = document.getElementById("bars");
    barsList.innerHTML = "";

    fetch("/bars").then( response => {
        response.json().then( bars => {
            bars.forEach( bar => {
                const li = document.createElement("li")
                li.innerText = bar.name;
                barsList.appendChild(li);
            });
        });
    });
}

window.addEventListener("load", () => {
    const form = document.getElementById("form");
    form.addEventListener("submit", event => {
        event.preventDefault();
        const name = document.getElementById("name");
        const req = {
            method: "post",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({"name": name.value})
        };
        fetch("/bars", req).then( () => {
            name.value = "";
            refreshBars();
        });
    });

    refreshBars();
});
