<?php

function youtube_title(string $id) {
  // $id = 'YOUTUBE_ID';
  $API_KEY   = "YOUR-API-KEY";
  $videoList = @json_decode(@file_get_contents("https://www.googleapis.com/youtube/v3/videos?part=snippet&id={$id}&key={$API_KEY}"));
  // despite @ suppress, it will be false if it fails
  if ($videoTitle) {
    // look for that title tag and get the insides
    preg_match("/<title>(.+?)<\/title>/is", $videoTitle, $titleOfVideo);
    return $titleOfVideo[1];
  } else {
    return false;
  }
  // usage:
  // $item = youtube_title('zgNJnBKMRNw');
}