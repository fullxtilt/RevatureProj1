currentUser = null;
reimbursementsLoaded = false;

window.onload = function () {

    getReimbursements();
    getCurrentUser();

    document.getElementById("filterSelect").addEventListener("change", onFilterChange);
}

async function getReimbursements() {
    const responsePayload = await fetch(`http://localhost:9006/api/reimbursements`);
    const reimbJSON = await responsePayload.json();

    displayReimbursements(reimbJSON);
}

async function getCurrentUser() {
    const responsePayload = await fetch(`http://localhost:9006/api/user/current`);
    const userJSON = await responsePayload.json();

    currentUser = userJSON;
    displayCurrentUser();
    attemptPostLoadManipulation();
}

// Update receipt as approved. String corresponds to enum value stored in server.
function acceptReimbursement(eve) {
    disableButtons(eve, "APPROVED");
}

// Update receipt as approved. String corresponds to enum value stored in server.
function denyReimbursement(eve) {
    disableButtons(eve, "DENIED");
}

// Disable our buttons to prevent duplicate requests
function disableButtons(eve, resolveStatus) {

    // Find both accept and deny buttons via their column's reimbID${id} class
    eve.target.setAttribute("disabled", "true");
    let targetClass;
    for (className of eve.path[1].classList) {
        if (className.includes("reimbButton"))
            targetClass = className;
    }
    // Disable them
    let buttonColumns = document.getElementsByClassName(targetClass);
    buttonColumns[0].firstChild.setAttribute("disabled", true);
    buttonColumns[1].firstChild.setAttribute("disabled", true);

    // send update to DB
    updateReimbursement(eve.path, resolveStatus);
}

// Sends given reimbursement's id and new status to the server to be updated. 
async function updateReimbursement(path, approve) {
    // To update our reimbursement, we need its id
    let reimbID = findReimbursementId(path);

    const formData = new FormData();
    formData.append("myID", reimbID);
    formData.append("reimbStatus", approve);

    const responsePayload = await fetch(`http://localhost:9006/api/reimbursements`, {
        method: "PATCH",
        body: formData
    });

    // Grab our updated reimbursement
    const reimbJSON = await responsePayload.json();

    updateRow(reimbJSON);
}

// Updates a given reimbursement in our table
function updateRow(reimbData) {
    // Grab the row
    let tableRow = document.getElementById(`reimbID${reimbData.myID}`);

    // If we couldn't find our row
    if (tableRow == null) {
        console.log("Searched for row with invalid id");
        return;
    }

    // Genearate an array of our reimbursement's fields
    let columnValues = generateColumnValues(reimbData);

    // Update reimbursement's row 
    for (let i = 0; i < columnValues.length && i + 1 < tableRow.children.length; i++) {
        tableRow.children[i + 1].innerText = columnValues[i];
    }

    // Remove approve/deny buttons
    let buttons = document.getElementsByClassName(`reimbButton${reimbData.myID}`);
    buttons[0].firstChild.setAttribute("disabled", true);
    buttons[1].firstChild.setAttribute("disabled", true);
}


function displayReimbursements(reimbJSON) {

    // Grab a reference to our table element
    let reimbTable = document.getElementById("reimbursementContainer");

    // Create td elements for each reimbursement
    for (reimbData of reimbJSON) {

        // Create our table row
        let newRow = document.createElement("tr");
        newRow.setAttribute("id", `reimbID${reimbData.myID}`);

        // Fill an array with our column values
        let columnValues = generateColumnValues(reimbData);
        let columnClasses = generateColumnClasses();

        // Create row's header
        let rowHeader = document.createElement("th");
        rowHeader.setAttribute("scope", "row");
        rowHeader.innerText = reimbData.myID;
        rowHeader.setAttribute("class", columnClasses[0]);
        newRow.appendChild(rowHeader);

        // Create a new table column for each value (aside from id, which is the header)
        for (let i = 0; i < columnValues.length; i++) {
            let columnValue = columnValues[i];
            let newColumn = document.createElement("td");
            newColumn.innerText = (columnValue != null) ? columnValue : "";
            newColumn.setAttribute("class", columnClasses[i + 1])

            // Add titles to user columns
            if (columnClasses[i + 1] == "reimbAuthor"
                || columnClasses[i + 1] == "reimbResolver") {
                // Use which data?
                let user;
                if (columnClasses[i + 1] == "reimbResolver")
                    user = reimbData.resolver;
                else
                    user = reimbData.author;

                // Set title
                if (user.firstName != null) {
                    newColumn.setAttribute("title", user.username);
                }
            }

            newRow.appendChild(newColumn);
        }

        // Give option to approve/deny if pending

        // Create accept and deny buttons
        let acceptButton = document.createElement("button");
        let denyButton = document.createElement("button");

        acceptButton.setAttribute("class", "btn btn-secondary");
        acceptButton.innerText = "Accept";
        acceptButton.addEventListener("click", acceptReimbursement);

        denyButton.setAttribute("class", "btn btn-secondary");
        denyButton.innerText = "Deny";
        denyButton.addEventListener("click", denyReimbursement);

        // Disable them if we aren't pending
        if (reimbData.reimbStatus != "PENDING") {
            acceptButton.setAttribute("disabled", "true");
            denyButton.setAttribute("disabled", "true");
        }

        // Append buttons to table
        let acceptColumn = document.createElement("td");
        acceptColumn.setAttribute("class", `resolveButton reimbButton${reimbData.myID}`);
        let denyColumn = document.createElement("td");
        denyColumn.setAttribute("class", `resolveButton reimbButton${reimbData.myID}`);

        acceptColumn.appendChild(acceptButton);
        denyColumn.appendChild(denyButton);

        newRow.appendChild(acceptColumn);
        newRow.appendChild(denyColumn);

        // Finally, append the row to our table
        reimbTable.appendChild(newRow);
    }

    /* 
        AFTER populating the reimbursement table, perform user-related
        DOM manipulations
    */
    reimbursementsLoaded = true;
    attemptPostLoadManipulation();
}

// Called when approving or denying a reimbursement
// Given a path in the reimbursement table, finds the table row and returns the reimbursement's id.
function findReimbursementId(path) {
    // To find the record's id, first find the parent row
    let parentRow = null;
    for (let tmp of path) {
        // Find the row
        if (tmp.localName == "tr") {
            parentRow = tmp;
            break;
        }
    }

    // If we never found a table row, something is very wrong
    if (parentRow == null) {
        console.log("Expected table row within path:", path);
        return;
    }

    let reimbId = parseInt(parentRow.firstChild.innerText);

    return reimbId;
}

function generateColumnValues(reimbData) {

    let resolvedTime = (new String(new Date(reimbData.resolvedTime))).substring(4, 15);

    return [
        reimbData.amount + "z",
        (reimbData.author.username) ? reimbData.author.firstName + " " + reimbData.author.lastName : "",
        (new String(new Date(reimbData.submittedTime))).substring(4, 15),
        reimbData.description,
        reimbData.reimbType,
        reimbData.reimbStatus,
        (reimbData.resolver.username) ? reimbData.resolver.firstName + " " + reimbData.resolver.lastName : "",
        (reimbData.resolvedTime) ? resolvedTime : ""
    ];
}

// Classes for each column in reimbursement table
function generateColumnClasses() {
    return [
        "reimbId",
        "reimbAmount",
        "reimbAuthor",
        "reimbSubmitted",
        "reimbDescription",
        "reimbType",
        "reimbStatus",
        "reimbResolver",
        "reimbResolved"
    ]
}

/* 
    Called from both onInit async functions
    displayCurrentUser won't be called until both are completed.
*/
function attemptPostLoadManipulation() {
    if (currentUser && reimbursementsLoaded)
        postLoadManipulation()
}

function displayCurrentUser() {
    let welcomer = document.getElementById("welcome");
    let title = (currentUser.role == "EMPLOYEE") ? "hunter: " : "handler: ";
    welcomer.innerText += " " + title + currentUser.firstName + " " + currentUser.lastName;

    // DOM manipulate different elements for employees and FMs
    if (currentUser.role == "EMPLOYEE") {
        // Show/Hide elements relevant to employee
        let resolveColumns = document.getElementsByClassName("resolveCol");
        for (col of resolveColumns) {
            col.style.display = "none";
        }
    }
    if (currentUser.role == "FINANCE_MANAGER") {
        // Hide create reimbursement navbar option
        let createReimbNav = document.getElementById("createReimbursement-nav");
        createReimbNav.style.display = "none";

        // Display filter button
        let filterSelect = document.getElementById("filterSelect");
        filterSelect.style.visibility = "visible";
    }
}

// Called after both onInit async functions have finished
// DOM manipulates objects that require both the reimbursement table and user
function postLoadManipulation() {
    if (currentUser.role == "EMPLOYEE") {
        // Hide accept/deny buttons
        let resolveButtons = document.getElementsByClassName("resolveButton");
        for (resolveButton of resolveButtons) {
            resolveButton.style.display = "none";
        }
    }
}

// Filters out reimbursements by status
function onFilterChange(eve) {
    let filterSelect = eve.target;

    // Get reimbursement table body
    let reimbTableBody = document.getElementById("reimbursementContainer");
    for (reimbRow of reimbTableBody.children) {
        // Find reimbursement's statuss
        let reimbStatus = reimbRow.getElementsByClassName("reimbStatus")[0];

        // if we aren't filtering for this status
        if (reimbStatus && reimbStatus.innerText != filterSelect.value
            && filterSelect.value != "unfiltered") {
            // Make row invisible
            reimbRow.style.display = "none";
        }
        else {
            reimbRow.style.display = "table-row";
        }
    }

}