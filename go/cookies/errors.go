package cookies

import (
	"fmt"

	"github.com/pkg/errors"
)

func Wrap(e error, m string, args ...interface{}) error {
	m = fmt.Sprintf(m, args...)
	return errors.Wrap(e, m)
}
