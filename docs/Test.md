---
layout: GCNS
---

## Test  
**This page is for testing only.  Please do not follow the content as it will be out of context.**
  
<br/>  
<br/>  

Now, we will update Linux on our [virtual machine](./NS_FreeTier.md), and install Nightscout on it.  You must have [FreeDNS](./FreeDNS.md) and [Google Project](./NS_GCProject.md) set up already to proceed.  
<br/>  
  
<!-- First input and button -->
<input type="text" value="curl https://example.com/script1.sh | bash" readonly 
       id="myInputText1" 
       style="border:none; color:#101010; background-color:#ededed; width:100%; font-size:15px">
<button onclick="copyText('myInputText1')" 
        style="border: 1px solid #0066ff; color:#f0f0f0; background: linear-gradient(#0066ff, #0066ff); font-size:14px; font-weight:400; border-radius: 2px; margin-left:70px; margin-top:8px; padding:4px 12px; display:inline-block; box-shadow: inset 0px 1px 0px rgba(255,255,255,.3), 0px 1px 5px rgba(0,0,0,.7);">Copy</button>

<br><br>

<!-- Second input and button -->
<input type="text" value="curl https://example.com/script2.sh | bash" readonly 
       id="myInputText2" 
       style="border:none; color:#101010; background-color:#ededed; width:100%; font-size:15px">
<button onclick="copyText('myInputText2')" 
        style="border: 1px solid #0066ff; color:#f0f0f0; background: linear-gradient(#0066ff, #0066ff); font-size:14px; font-weight:400; border-radius: 2px; margin-left:70px; margin-top:8px; padding:4px 12px; display:inline-block; box-shadow: inset 0px 1px 0px rgba(255,255,255,.3), 0px 1px 5px rgba(0,0,0,.7);">Copy</button>

<script>
function copyText(inputId) {
    var copyField = document.getElementById(inputId);
    copyField.select();
    copyField.setSelectionRange(0, 99999); // for mobile
    document.execCommand("copy");
    alert("Copied: " + copyField.value);
}
</script>

