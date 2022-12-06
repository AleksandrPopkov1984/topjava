const mealAjaxUrl = "ui/meals/";

const ctx = {
    ajaxUrl: mealAjaxUrl
};

$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ]
        })
    );
});

$('#filterDetails').submit(function () {
    filterTable();
    return false;
});

function filterTable() {
    $.ajax({
        type: "GET",
        url: ctx.ajaxUrl + 'filter',
        data: $('#filterDetails').serialize(),
    }).done(function (data) {
        updateTableByData(data);
        successNoty("Filter is applied")
    })
}

function updateTableByData(data) {
    ctx.datatableApi.clear().rows.add(data).draw();
}

$('#filter_reset').click(function () {
    resetButtonBind();
    filterTable();
});

function resetButtonBind() {
    $('#filterDetails')[0].reset();
}