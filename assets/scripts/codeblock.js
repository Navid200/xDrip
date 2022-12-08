

var codeBlocks = document.querySelectorAll('pre.highlight');


codeBlocks.forEach(function (codeBlock) {
  var  Button = document.createElement('button');
   Button.className = ' ';
   Button.type = 'button';
   Button.ariaLabel = 'Copy code to clipboard';
   Button.innerText = 'Copy';


  codeBlock.append( Button);


   Button.addEventListener('click', function () {
    var code = codeBlock.querySelector('code').innerText.trim();
    window.navigator.clipboard.writeText(code);


     Button.innerText = 'Copied';
    var fourSeconds = 4000;


    setTimeout(function () {
       Button.innerText = 'Copy';
    }, fourSeconds);
  });
});
