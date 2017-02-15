import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'noat'
})
export class NoatPipe implements PipeTransform {

  transform(value: string, args?: any): any {
    if(value == null)
      return value;

    if (value.startsWith('@'))
      return value.substring(1);
    else
      return value;
  }

}
