function copyText(inputId, msgId) {
    var copyField = document.getElementById(inputId);

    // Modern way: copy without selecting
    navigator.clipboard.writeText(copyField.value).then(function() {
        // Show "Copied!" message
        var msg = document.getElementById(msgId);
        msg.style.display = 'inline';
        setTimeout(function(){
            msg.style.display = 'none';
        }, 1500);
    }).catch(function(err) {
        console.error('Copy failed', err);
    });
}
