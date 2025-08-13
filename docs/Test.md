---
layout: GCNS
---

## Test  
**This page is for testing only.  Please do not follow the content as it will be out of context.**
  
<br/>  
<br/>  




<!-- First input and button -->
<input type="text" value="curl https://example.com/script1.sh | bash" readonly 
       id="myInputText1" 
       style="border:none; color:#101010; background-color:#ededed; width:100%; font-size:15px">
<button onclick="copyText('myInputText1')" 
        style="border: 1px solid #0066ff; color:#f0f0f0; background: linear-gradient(#0066ff, #0066ff); font-size:14px; font-weight:400; border-radius: 2px; margin-left:70px; margin-top:8px; padding:4px 12px; display:inline-block; box-shadow: inset 0px 1px 0px rgba(255,255,255,.3), 0px 1px 5px rgba(0,0,0,.7);">Copy</button>
<span id="msg1" style="margin-left:10px; color:green; display:none;">Copied!</span>

<br><br>

<!-- Second input and button -->
<input type="text" value="curl https://example.com/script2.sh | bash" readonly 
       id="myInputText2" 
       style="border:none; color:#101010; background-color:#ededed; width:100%; font-size:15px">
<button onclick="copyText('myInputText2', 'msg2')" 
        style="border: 1px solid #0066ff; color:#f0f0f0; background: linear-gradient(#0066ff, #0066ff); font-size:14px; font-weight:400; border-radius: 2px; margin-left:70px; margin-top:8px; padding:4px 12px; display:inline-block; box-shadow: inset 0px 1px 0px rgba(255,255,255,.3), 0px 1px 5px rgba(0,0,0,.7);">Copy</button>
<span id="msg2" style="margin-left:10px; color:green; display:none;">Copied!</span>

<script>
function copyText(inputId, msgId) {
    var copyField = document.getElementById(inputId);
    copyField.select();
    copyField.setSelectionRange(0, 99999); // for mobile
    document.execCommand("copy");

    // Show "Copied!" message briefly
    var msg = document.getElementById(msgId);
    msg.style.display = 'inline';
    setTimeout(function(){
        msg.style.display = 'none';
    }, 1500); // hides after 1.5 seconds
}
</script>

