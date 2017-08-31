<?hh

function a(int $x): int {
  return $x + 1;
}

function main(): void {
  a(5);
  
  // No errors!
  echo a(5);
}

main();
