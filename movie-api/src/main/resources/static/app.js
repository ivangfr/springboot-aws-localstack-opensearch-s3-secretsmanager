$(function () {
    $('.message').on('click', function() {
        $(this).closest('.message').transition('fade')
    })
})
