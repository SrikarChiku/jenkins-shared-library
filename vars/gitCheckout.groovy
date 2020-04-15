def gitCheckout(branch,url){
    echo "${branch} ${url}"
    git branch: "${branch}", url: "${url}"
}
